#!/usr/bin/env python

# This file is part of VoltDB.
# Copyright (C) 2008-2020 VoltDB Inc.
#
# Permission is hereby granted, free of charge, to any person obtaining
# a copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so, subject to
# the following conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
# OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
# ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.


# This file contains the schema for running SqlCoverage tests against a
# file of SQL statements randomly generated by the SQL-grammar-generator
# test application; as such, it is consistent with:
#   voltdb/tests/sqlcoverage/ddl/sql-grammar-gen-DDL.sql, i.e.,
#   voltdb/tests/sqlgrammar/DDL.sql.

{
    "P0": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ()
        },
    "R0": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ()
        },
    "P1": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "R1": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "P2": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "R2": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "P3": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "R3": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "P4": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "R4": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "P5": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        },
    "R5": {
        "columns": (("ID", FastSerializer.VOLTTYPE_INTEGER),
                    ("TINY", FastSerializer.VOLTTYPE_TINYINT),
                    ("SMALL", FastSerializer.VOLTTYPE_SMALLINT),
                    ("INT", FastSerializer.VOLTTYPE_INTEGER),
                    ("BIG", FastSerializer.VOLTTYPE_BIGINT),
                    ("NUM", FastSerializer.VOLTTYPE_FLOAT),
                    ("DEC", FastSerializer.VOLTTYPE_DECIMAL),
                    ("VCHAR_INLINE", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_INLINE_MAX", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR", FastSerializer.VOLTTYPE_STRING),
                    ("VCHAR_JSON", FastSerializer.VOLTTYPE_STRING),
                    ("TIME", FastSerializer.VOLTTYPE_TIMESTAMP),
                    ("VARBIN", FastSerializer.VOLTTYPE_VARBINARY),
                    ("POINT", FastSerializer.VOLTTYPE_GEOGRAPHY_POINT),
                    ("POLYGON", FastSerializer.VOLTTYPE_GEOGRAPHY)),
        "partitions": (),
        "indexes": ("ID")
        }
}
