package com.johnpili.crud.sqlite.constants;

public abstract class SqlStatements {
    public static final String LAST_INSERT_ID = "SELECT last_insert_rowid()";

    public static final String GET_TABLES = "SELECT name FROM sqlite_schema WHERE type = 'table' ORDER BY name";

    public static final String GET_TABLE_BY_NAME = "SELECT name FROM sqlite_schema WHERE type = 'table' AND name = ?";
}
