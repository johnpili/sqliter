package com.johnpili.crud.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISqliteObjectAssembler<T> {
    /**
     * This method is used for converting SQLite result into POJO via dependency injection
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    T assemble(ResultSet resultSet) throws SQLException;
}
