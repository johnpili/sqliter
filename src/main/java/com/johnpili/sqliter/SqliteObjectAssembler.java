package com.johnpili.sqliter;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqliteObjectAssembler<T> {
    /**
     * This method is used for converting SQLite result into POJO via dependency injection
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    T assemble(ResultSet resultSet) throws SQLException;
}
