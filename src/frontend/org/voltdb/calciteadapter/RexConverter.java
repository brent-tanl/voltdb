/* This file is part of VoltDB.
 * Copyright (C) 2008-2017 VoltDB Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.voltdb.calciteadapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelRecordType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexDynamicParam;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexLocalRef;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexProgram;
import org.apache.calcite.rex.RexVisitorImpl;
import org.apache.calcite.sql.fun.SqlDatetimeSubtractionOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.IntervalSqlType;
import org.apache.calcite.util.NlsString;
import org.apache.calcite.util.Pair;
import org.voltdb.VoltType;
import org.voltdb.catalog.Column;
import org.voltdb.expressions.AbstractExpression;
import org.voltdb.expressions.ComparisonExpression;
import org.voltdb.expressions.ConjunctionExpression;
import org.voltdb.expressions.ConstantValueExpression;
import org.voltdb.expressions.ExpressionUtil;
import org.voltdb.expressions.OperatorExpression;
import org.voltdb.expressions.ParameterValueExpression;
import org.voltdb.expressions.TupleValueExpression;
import org.voltdb.plannodes.NodeSchema;
import org.voltdb.plannodes.SchemaColumn;
import org.voltdb.types.ExpressionType;

public class RexConverter {

    private static int NEXT_PARAMETER_ID = 0;

    public static void resetParameterIndex() {
        NEXT_PARAMETER_ID = 0;
    }

    private static class ConvertingVisitor extends RexVisitorImpl<AbstractExpression> {

        public static final ConvertingVisitor INSTANCE = new ConvertingVisitor();

        protected int m_numLhsFieldsForJoin = -1;

        protected ConvertingVisitor() {
            super(false);
        }

        public ConvertingVisitor(int numLhsFields) {
            super(false);
            m_numLhsFieldsForJoin = numLhsFields;
        }

        protected boolean isFromRHSTable(int columnIndex) {
            return m_numLhsFieldsForJoin >= 0 && columnIndex >= m_numLhsFieldsForJoin;
        }

        protected TupleValueExpression visitInputRef(RexInputRef inputRef, String tableName, String columnName) {
            int columnIndex = inputRef.getIndex();
            int tableIndex = 0;
            if (isFromRHSTable(columnIndex)) {
                columnIndex -= m_numLhsFieldsForJoin;
                tableIndex = 1;
            }

            if (tableName == null) {
                tableName = "";
            }
            if (columnName == null) {
                // Generate a column name out of its index in the original table 1 -> "001"
                columnName = String.format("%03d", columnIndex);
            }

            TupleValueExpression tve = new TupleValueExpression(tableName, tableName, columnName, columnName, columnIndex, columnIndex);
            tve.setTableIndex(tableIndex);
            TypeConverter.setType(tve, inputRef.getType());
            return tve;
        }

        @Override
        public TupleValueExpression visitInputRef(RexInputRef inputRef) {
            return visitInputRef(inputRef, null, null);
        }

        @Override
        public ParameterValueExpression visitDynamicParam(RexDynamicParam inputParam) {
            ParameterValueExpression pve = new ParameterValueExpression();
            pve.setParameterIndex(NEXT_PARAMETER_ID++);
            TypeConverter.setType(pve, inputParam.getType());
            return pve;
        }

        @Override
        public ConstantValueExpression visitLiteral(RexLiteral literal) {
            ConstantValueExpression cve = new ConstantValueExpression();

            String value = null;
            if (literal.getValue() instanceof NlsString) {
                NlsString nlsString = (NlsString) literal.getValue();
                value = nlsString.getValue();
            } else if (literal.getValue() instanceof BigDecimal) {
                BigDecimal bd = (BigDecimal) literal.getValue();
                // Special treatment for intervals - VoltDB TIMESTAMP expects value in microseconds
                if (literal.getType() instanceof IntervalSqlType) {
                    BigDecimal thousand = BigDecimal.valueOf(1000);
                    bd = bd.multiply(thousand);
                }
                value = bd.toPlainString();
            } else if (literal.getValue() instanceof GregorianCalendar) {
                // VoltDB TIMESTAMPS expects time in microseconds
                long time = ((GregorianCalendar) literal.getValue()).getTimeInMillis() * 1000;
                value = Long.toString(time);
            }

            assert value != null;

            cve.setValue(value);
            TypeConverter.setType(cve, literal.getType());

            return cve;
        }

        @Override
        public AbstractExpression visitCall(RexCall call) {

            List<AbstractExpression> aeOperands = new ArrayList<>();
            for (RexNode operand : call.operands) {
                AbstractExpression ae = operand.accept(this);
                assert ae != null;
                aeOperands.add(ae);
            }

            AbstractExpression ae = null;
            switch (call.op.kind) {
            // Conjunction
            case AND:
                    ae = new ConjunctionExpression(
                            ExpressionType.CONJUNCTION_AND,
                            aeOperands.get(0),
                            aeOperands.get(1));
                break;
            case OR:
                if (aeOperands.size() == 2) {
                    // Binary OR
                    ae = new ConjunctionExpression(
                            ExpressionType.CONJUNCTION_OR,
                            aeOperands.get(0),
                            aeOperands.get(1));
                } else {
                    // COMPARE_IN
                    ae = RexConverterHelper.createInComparisonExpression(aeOperands);
                }
                break;

            // Binary Comparison
            case EQUALS:
                    ae = new ComparisonExpression(
                            ExpressionType.COMPARE_EQUAL,
                            aeOperands.get(0),
                            aeOperands.get(1));
                break;
            case NOT_EQUALS:
                ae = new ComparisonExpression(
                        ExpressionType.COMPARE_NOTEQUAL,
                        aeOperands.get(0),
                        aeOperands.get(1));
                break;
            case LESS_THAN:
                ae = new ComparisonExpression(
                        ExpressionType.COMPARE_LESSTHAN,
                        aeOperands.get(0),
                        aeOperands.get(1));
                break;
            case GREATER_THAN:
                ae = new ComparisonExpression(
                        ExpressionType.COMPARE_GREATERTHAN,
                        aeOperands.get(0),
                        aeOperands.get(1));
                break;
            case LESS_THAN_OR_EQUAL:
                ae = new ComparisonExpression(
                        ExpressionType.COMPARE_LESSTHANOREQUALTO,
                        aeOperands.get(0),
                        aeOperands.get(1));
                break;
            case GREATER_THAN_OR_EQUAL:
                ae = new ComparisonExpression(
                        ExpressionType.COMPARE_GREATERTHANOREQUALTO,
                        aeOperands.get(0),
                        aeOperands.get(1));
                break;
            case LIKE:
                ae = new ComparisonExpression(
                        ExpressionType.COMPARE_LIKE,
                        aeOperands.get(0),
                        aeOperands.get(1));
                break;
//            COMPARE_NOTDISTINCT          (ComparisonExpression.class, 19, "NOT DISTINCT", true),

             // Arthimetic Operators
            case PLUS:
                // Check for DATETIME + INTERVAL expression first
                if (SqlStdOperatorTable.DATETIME_PLUS.getName().equals(call.op.getName())) {
                    // At this point left and right operands are converted to MICROSECONDS
                    ae = RexConverterHelper.createToTimestampFunctionExpression(
                            call.getType(),
                            ExpressionType.OPERATOR_PLUS,
                            aeOperands);
                } else {
                    ae = new OperatorExpression(
                            ExpressionType.OPERATOR_PLUS,
                            aeOperands.get(0),
                            aeOperands.get(1));
                }
                break;
            case MINUS:
                // Check for DATETIME - INTERVAL expression first
                // For whatever reason Calcite treats + and - DATETIME operation differently
                if (call.op instanceof SqlDatetimeSubtractionOperator) {
                    ae = RexConverterHelper.createToTimestampFunctionExpression(
                            call.getType(),
                            ExpressionType.OPERATOR_MINUS,
                            aeOperands);
                } else {
                    ae = new OperatorExpression(
                            ExpressionType.OPERATOR_MINUS,
                            aeOperands.get(0),
                            aeOperands.get(1));
                }
                break;
            case TIMES:
                ae = new OperatorExpression(
                            ExpressionType.OPERATOR_MULTIPLY,
                            aeOperands.get(0),
                            aeOperands.get(1));
                break;
            case DIVIDE:
                ae = new OperatorExpression(
                            ExpressionType.OPERATOR_DIVIDE,
                            aeOperands.get(0),
                            aeOperands.get(1));
                break;
            case CAST:
                ae = new OperatorExpression(
                            ExpressionType.OPERATOR_CAST,
                            aeOperands.get(0),
                            null);
                TypeConverter.setType(ae, call.getType());
                break;
            case NOT:
                ae = new OperatorExpression(
                            ExpressionType.OPERATOR_NOT,
                            aeOperands.get(0),
                            null);
                TypeConverter.setType(ae, call.getType());
                break;
            case IS_NULL:
                ae = new OperatorExpression(
                            ExpressionType.OPERATOR_IS_NULL,
                            aeOperands.get(0),
                            null);
                TypeConverter.setType(ae, call.getType());
                break;
            case EXISTS:
                ae = new OperatorExpression(
                            ExpressionType.OPERATOR_EXISTS,
                            aeOperands.get(0),
                            null);
                TypeConverter.setType(ae, call.getType());
                break;

//            OPERATOR_CONCAT                (OperatorExpression.class,  5, "||"),
//                // left || right (both must be char/varchar)
//            OPERATOR_MOD                   (OperatorExpression.class,  6, "%"),
//                // left % right (both must be integer)

            case OTHER:
                if ("||".equals(call.op.getName())) {
                    // CONCAT
                    ae = RexConverterHelper.createFunctionExpression(call.getType(), "concat", aeOperands, null);
                    TypeConverter.setType(ae, call.getType());
                } else {
                    throw new CalcitePlanningException("Unsupported Calcite expression type: " +
                            call.op.kind.toString());
                }
                break;
            default:
                throw new CalcitePlanningException("Unsupported Calcite expression type: " +
                        call.op.kind.toString());
            }

            assert ae != null;
            assert ae.getValueType() != VoltType.INVALID;
            ExpressionUtil.finalizeValueTypes(ae);
            return ae;
        }

    }

    /**
     * Resolve filter expression for a standalone table (numLhsFieldsForJoin = -1)
     * or outer table from a join (possibly inline inner node for NLIJ).
     * The resolved expression is used to identify a suitable index to access the data
     *
     */
    private static class RefExpressionConvertingVisitor extends ConvertingVisitor {

        private List<RexNode> m_exprList = null;
        private List<Column> m_catColumns = null;
        private String m_catTableName = "";

        public RefExpressionConvertingVisitor(String catTableName, List<Column> catColumns, List<RexNode> exprList, int numLhsFieldsForJoin) {
            super(numLhsFieldsForJoin);
            m_catTableName = catTableName;
            m_catColumns = catColumns;
            m_exprList = exprList;
        }

        public RefExpressionConvertingVisitor(String catTableName, List<Column> catColumns, List<RexNode> exprList) {
            this(catTableName, catColumns, exprList, -1);
        }

        @Override
        public AbstractExpression visitLocalRef(RexLocalRef localRef) {
            assert(m_exprList != null);
            int exprIndx = localRef.getIndex();
            if (isFromRHSTable(exprIndx)) {
                exprIndx -= m_numLhsFieldsForJoin;
            }

            assert(exprIndx < m_exprList.size());
            RexNode expr = m_exprList.get(exprIndx);
            return expr.accept(this);
        }

        @Override
        public TupleValueExpression visitInputRef(RexInputRef inputRef) {
            int exprIndx = inputRef.getIndex();

            String columnName = null;
            String tableName = null;
            // Resolve column name if it is not a join or it's inner table from a join
            if (isFromRHSTable(exprIndx) || m_numLhsFieldsForJoin < 0) {
                exprIndx -= (m_numLhsFieldsForJoin < 0) ? 0 : m_numLhsFieldsForJoin;
                if (m_catColumns != null && exprIndx < m_catColumns.size()) {
                    columnName = m_catColumns.get(exprIndx).getTypeName();
                }
                tableName = m_catTableName;
            }

            return visitInputRef(inputRef, tableName, columnName);
        }
    }

    public static NodeSchema convertToVoltDBNodeSchema(RexProgram program) {
        NodeSchema newNodeSchema = new NodeSchema();
        int i = 0;

        for (Pair<RexLocalRef, String> item : program.getNamedProjects()) {
            String name = item.right;
            RexNode rexNode = program.expandLocalRef(item.left);
            AbstractExpression ae = rexNode.accept(ConvertingVisitor.INSTANCE);
            assert (ae != null);

            newNodeSchema.addColumn(new SchemaColumn("", "", "", name, ae, i));
            ++i;
        }

        return newNodeSchema;
    }

    public static AbstractExpression convert(RexNode rexNode) {
        AbstractExpression ae = rexNode.accept(ConvertingVisitor.INSTANCE);
        assert ae != null;
        return ae;
    }

    public static AbstractExpression convertJoinPred(int numLhsFields,
            RexNode condition) {
        AbstractExpression ae = condition.accept(new ConvertingVisitor(numLhsFields));
        assert ae != null;
        return ae;
    }

    public static NodeSchema convertToVoltDBNodeSchema(RelDataType rowType) {
        NodeSchema nodeSchema = new NodeSchema();

        RelRecordType ty = (RelRecordType) rowType;
        List<String> names = ty.getFieldNames();
        int i = 0;
        for (RelDataTypeField item : ty.getFieldList()) {
            TupleValueExpression tve = new TupleValueExpression("", "", "", names.get(i), i, i);
            TypeConverter.setType(tve, item.getType());
            nodeSchema.addColumn(new SchemaColumn("", "", "", names.get(i), tve, i));
            ++i;
        }
        return nodeSchema;
    }

    public static NodeSchema convertToVoltDBNodeSchema(
            List<Pair<RexNode, String>> namedProjects) {
        NodeSchema nodeSchema = new NodeSchema();
        int i = 0;
        for (Pair<RexNode, String> item : namedProjects) {
            AbstractExpression ae = item.left.accept(ConvertingVisitor.INSTANCE);
            nodeSchema.addColumn(new SchemaColumn("", "", "", item.right, ae, i));
        }

        return nodeSchema;
    }

    /**
     * Given a conditional RexNodes representing reference expressions ($1 > $2) convert it into
     * a corresponding TVE. If the numLhsFieldsForJoin is set to something other than -1 it means
     * that this table is an inner table of some join and its expression indexes must be adjusted
     *
     * @param condition RexNode to be converted
     * @param catTableName a catalog table name
     * @param catColumns column name list
     * @param exprs list of all expressions that are associated with this table
     * @param numLhsFieldsForJoin number of fields that come from outer join (-1 if not a join)

     * @return
     */
    public static AbstractExpression convertRefExpression(
            RexNode condition, String catTableName, List<Column> catColumns, List<RexNode> exprs, int numLhsFieldsForJoin) {
        AbstractExpression ae = condition.accept(
                new RefExpressionConvertingVisitor(catTableName, catColumns, exprs, numLhsFieldsForJoin));
        assert ae != null;
        return ae;
    }

    /**
     * Given an AbstractExpression convert it into a Calcite reference expression
     * @param expression
     * @param program
     * @return
     */
    public static RexLocalRef convertAbstractExpression(AbstractExpression expression, RexProgram program) {
        // @TODO
        assert(false);
        return null;
    }

    public static RexNode convertAbstractExpression(AbstractExpression expression) {
        assert(expression != null);
        // @TODO
        assert(false);
//        RexNode op1 = null;
//        RexNode op2 = null;
//        if (expression.getLeft() != null) {
//            op1 = convertAbstractExpression(expression.getLeft());
//        }
//        if (expression.getRight() != null) {
//            op2 = convertAbstractExpression(expression.getRight());
//        }
//        switch (expression.getExpressionType()) {
//            // ----------------------------
//            // Arthimetic Operators
//            // ----------------------------
//            case OPERATOR_PLUS  : return
//                    new RexCall(TypeConverter.voltTypeToSqlType(expression.getValueType(), );
//
//                // left + right (both must be number. implicitly casted)
//            OPERATOR_MINUS                 (OperatorExpression.class,  2, "-"),
//                // left - right (both must be number. implicitly casted)
//            OPERATOR_MULTIPLY              (OperatorExpression.class,  3, "*"),
//                // left * right (both must be number. implicitly casted)
//            OPERATOR_DIVIDE                (OperatorExpression.class,  4, "/"),
//                // left / right (both must be number. implicitly casted)
//            OPERATOR_CONCAT                (OperatorExpression.class,  5, "||"),
//                // left || right (both must be char/varchar)
//            OPERATOR_MOD                   (OperatorExpression.class,  6, "%"),
//                // left % right (both must be integer)
//            OPERATOR_CAST                  (OperatorExpression.class,  7, "<cast>"),
//                // explicitly cast left as right (right is integer in ValueType enum)
//            OPERATOR_NOT                   (OperatorExpression.class,  8, "NOT", true),
//                // logical not
//            OPERATOR_IS_NULL               (OperatorExpression.class,  9, "IS NULL", true),
//            // unary null evaluation
//            OPERATOR_EXISTS                (OperatorExpression.class, 18, "EXISTS", true),
//            // unary exists evaluation
//            // 19 is assigned to COMPARE_NOTDISTINCT, 20, 21 to CONJUNCTION_AND and CONJUNCTION_OR
//            OPERATOR_UNARY_MINUS           (OperatorExpression.class, 22, "UNARY MINUS", true),
//            // unary exists evaluation
//
//            default : return null;
//        }
        return null;
    }

}
