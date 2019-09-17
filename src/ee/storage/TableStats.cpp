/* This file is part of VoltDB.
 * Copyright (C) 2008-2019 VoltDB Inc.
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
#include "storage/TableStats.h"
#include "stats/StatsSource.h"
#include "common/TupleSchema.h"
#include "common/ids.h"
#include "common/ValueFactory.hpp"
#include "common/tabletuple.h"
#include "storage/table.h"
#include "storage/persistenttable.h"
#include "storage/tablefactory.h"
#include <vector>
#include <string>
#include <math.h>

using namespace voltdb;
using namespace std;

array<StatsSource::schema_tuple_type, 8> const TableStats::BASE_SCHEMA = {
    make_tuple("TABLE_NAME", ValueType::tVARCHAR, 4096, false, false),
    make_tuple("TABLE_TYPE", ValueType::tVARCHAR, 4096, false, false),

    make_tuple("TUPLE_COUNT", ValueType::tBIGINT, NValue::getTupleStorageSize(ValueType::tBIGINT), false, false),
    make_tuple("TUPLE_ALLOCATED_MEMORY", ValueType::tBIGINT, NValue::getTupleStorageSize(ValueType::tBIGINT), false, false),
    make_tuple("TUPLE_DATA_MEMORY", ValueType::tBIGINT, NValue::getTupleStorageSize(ValueType::tBIGINT), false, false),
    make_tuple("STRING_DATA_MEMORY", ValueType::tBIGINT, NValue::getTupleStorageSize(ValueType::tBIGINT), false, false),

    make_tuple("TUPLE_LIMIT", ValueType::tINTEGER, NValue::getTupleStorageSize(ValueType::tINTEGER), false, false),
    make_tuple("PERCENT_FULL", ValueType::tINTEGER, NValue::getTupleStorageSize(ValueType::tINTEGER), false, false)
};

vector<string> TableStats::generateStatsColumnNames() {
    vector<string> columnNames = StatsSource::generateStatsColumnNames();
    for (auto const& t : BASE_SCHEMA) {
        columnNames.emplace_back(get<0>(t));
    }
    return columnNames;
}

// make sure to update schema in frontend sources (like TableStats.java) and tests when updating
// the table-stats schema in here.
StatsSource::schema_type TableStats::populateTableStatsSchema() {
    vector<string> names;
    vector<ValueType> types;
    vector<int32_t> columnLengths;
    vector<bool> allowNull, inBytes;
    tie(names, types, columnLengths, allowNull, inBytes) = StatsSource::populateBaseSchema();
    for (auto const& t : BASE_SCHEMA) {
        names.emplace_back(get<0>(t));
        types.emplace_back(get<1>(t));
        columnLengths.emplace_back(get<2>(t));
        allowNull.push_back(get<3>(t));
        inBytes.push_back(get<4>(t));
    }
    return make_tuple(names, types, columnLengths, allowNull, inBytes);
}

TempTable* TableStats::generateEmptyTableStatsTable() {
    string name = "Persistent Table aggregated table stats temp table";
    vector<string> columnNames;
    vector<ValueType> columnTypes;
    vector<int32_t> columnLengths;
    vector<bool> columnAllowNull;
    vector<bool> columnInBytes;
    tie(columnNames, columnTypes, columnLengths, columnAllowNull, columnInBytes) =
        populateTableStatsSchema();
    TupleSchema *schema = TupleSchema::createTupleSchema(
            columnTypes, columnLengths, columnAllowNull, columnInBytes);
    return TableFactory::buildTempTable(name, schema, columnNames, nullptr);
}

/*
 * Constructor caches reference to the table that will be generating the statistics
 */
TableStats::TableStats(Table* table) : StatsSource(), m_table(table) {}

/**
 * Configure a StatsSource superclass for a set of statistics. Since this class is only used in the EE it can be assumed that
 * it is part of an Execution Site and that there is a site Id.
 * @parameter name Name of this set of statistics
 * @parameter hostId id of the host this partition is on
 * @parameter hostname name of the host this partition is on
 * @parameter siteId this stat source is associated with
 * @parameter partitionId this stat source is associated with
 * @parameter databaseId Database this source is associated with
 */
void TableStats::configure(std::string const& name) {
    StatsSource::configure(name);
    m_tableName = ValueFactory::getStringValue(m_table->name());
    m_tableType = ValueFactory::getStringValue(m_table->tableType());
}

/**
 * Update the stats tuple with the latest statistics available to this StatsSource.
 */
void TableStats::updateStatsTuple(TableTuple *tuple) {
    tuple->setNValue( StatsSource::m_columnName2Index["TABLE_NAME"], m_tableName);
    tuple->setNValue( StatsSource::m_columnName2Index["TABLE_TYPE"], m_tableType);
    int64_t tupleCount = m_table->activeTupleCount();
    int tupleLimit = m_table->tupleLimit();
    // This overflow is unlikely (requires 2 terabytes of allocated string memory)
    int64_t allocated_tuple_mem_kb = m_table->allocatedTupleMemory() / 1024;
    int64_t occupied_tuple_mem_kb = 0;
    PersistentTable* persistentTable = dynamic_cast<PersistentTable*>(m_table);
    if (persistentTable) {
        occupied_tuple_mem_kb = persistentTable->occupiedTupleMemory() / 1024;
    }
    int64_t string_data_mem_kb = m_table->nonInlinedMemorySize() / 1024;

    if (interval()) {
        tupleCount = tupleCount - m_lastTupleCount;
        m_lastTupleCount = m_table->activeTupleCount();
        allocated_tuple_mem_kb =
            allocated_tuple_mem_kb - (m_lastAllocatedTupleMemory / 1024);
        m_lastAllocatedTupleMemory = m_table->allocatedTupleMemory();
        occupied_tuple_mem_kb =
            occupied_tuple_mem_kb - (m_lastOccupiedTupleMemory / 1024);
        if (persistentTable) {
            m_lastOccupiedTupleMemory = persistentTable->occupiedTupleMemory();
        }
        string_data_mem_kb =
            string_data_mem_kb - (m_lastStringDataMemory / 1024);
        m_lastStringDataMemory = m_table->nonInlinedMemorySize();
    }

    tuple->setNValue(StatsSource::m_columnName2Index["TUPLE_COUNT"],
            ValueFactory::getBigIntValue(tupleCount));
    tuple->setNValue(StatsSource::m_columnName2Index["TUPLE_ALLOCATED_MEMORY"],
            ValueFactory::getBigIntValue(allocated_tuple_mem_kb));
    tuple->setNValue(StatsSource::m_columnName2Index["TUPLE_DATA_MEMORY"],
            ValueFactory::getBigIntValue(occupied_tuple_mem_kb));
    tuple->setNValue(StatsSource::m_columnName2Index["STRING_DATA_MEMORY"],
            ValueFactory::getBigIntValue(string_data_mem_kb));

    bool const hasTupleLimit = tupleLimit != INT_MAX;
    tuple->setNValue(StatsSource::m_columnName2Index["TUPLE_LIMIT"],
            hasTupleLimit ? ValueFactory::getIntegerValue(tupleLimit) : ValueFactory::getNullValue());
    int32_t percentage = 0;
    if (hasTupleLimit && tupleLimit > 0) {
        percentage = static_cast<int32_t>(ceil(tupleCount * 100. / tupleLimit));
    }
    tuple->setNValue(StatsSource::m_columnName2Index["PERCENT_FULL"],
            ValueFactory::getIntegerValue(percentage));
}

/**
 * Same pattern as generateStatsColumnNames except the return value is used as an offset into the tuple schema instead of appending to
 * end of a list.
 */
StatsSource::schema_type TableStats::populateSchema() {
    return populateTableStatsSchema();
}

TableStats::~TableStats() {
    m_tableName.free();
    m_tableType.free();
}
