package com.johnpili.sqliter;

import com.johnpili.sqliter.exceptions.SqliteDriverNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SqliteRepositoryInterface {

    //region Connection Methods

    /**
     * This method initialize the SQLite connection and driver check
     *
     * @return Connection
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    Connection getConnection() throws SQLException, SqliteDriverNotFoundException;

    /**
     * This method initialize the SQLite connection and driver check
     *
     * @param pragmaFk boolean
     * @return Connection
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    Connection getConnection(boolean pragmaFk) throws SQLException, SqliteDriverNotFoundException;

    //endregion

    //region DML Methods

    /**
     * Insert into SQLite and return the generated primary ID.
     * Method will return -1 if an error occurs during execution
     *
     * @param sql        String
     * @param parameters Map&lt;Integer, Object&gt;
     * @return int
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    int insert(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Update rows in SQLite and return the number of affected rows
     *
     * @param sql        String
     * @param parameters Map&lt;Integer, Object&gt;
     * @return int
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    int update(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Delete rows in SQLite and return the number of affected rows
     *
     * @param sql        String
     * @param parameters Map&lt;Integer, Object&gt;
     * @return int
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    int delete(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException;

    //endregion

    //region DQL METHODS

    /**
     * Query a single item using sql. Convert resultset to POJO via dependency injection
     *
     * @param sql                   String
     * @param sqliteObjectAssembler SqliteObjectAssembler
     * @return returns an Object of T or null
     * @param <T> generic type
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    <T> T getSingle(String sql, SqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Query a single item using sql and map of parameters. Convert resultset to POJO via dependency injection
     *
     * @param sql                   String
     * @param parameters            Map&lt;Integer, Object&gt;
     * @param sqliteObjectAssembler SqliteObjectAssembler
     * @return returns an Object or null
     * @param <T> generic type
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    <T> T getSingle(String sql, Map<Integer, Object> parameters, SqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Query a list items using sql. Convert resultset to POJO via dependency injection
     *
     * @param sql                   String
     * @param sqliteObjectAssembler SqliteObjectAssembler
     * @return returns a List&lt;T&gt; or an empty list
     * @param <T> generic type
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    <T> List<T> getList(String sql, SqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Query a list items using sql and map of parameters. Convert resultset to POJO via dependency injection
     *
     * @param sql                   String
     * @param parameters            Map&lt;Integer, Object&gt;
     * @param sqliteObjectAssembler SqliteObjectAssembler
     * @return returns a List&lt;T&gt; or an empty list
     * @param <T> generic type
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    <T> List<T> getList(String sql, Map<Integer, Object> parameters, SqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException;

    //endregion

    //region DDL METHODS

    /**
     * Method that handles create table
     *
     * @param sql String
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    void createTable(String sql) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Method that handles alter table
     *
     * @param sql String
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    void alterTable(String sql) throws SQLException, SqliteDriverNotFoundException;

    /**
     * Method that handles drop table
     *
     * @param sql String
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    void dropTable(String sql) throws SQLException, SqliteDriverNotFoundException;

    //endregion

    /**
     * Get list of tables from SQLite
     *
     * @return return a List&lt;String&gt;
     * @throws SQLException                  SQLException
     * @throws SqliteDriverNotFoundException SqliteDriverNotFoundException
     */
    List<String> getTables() throws SQLException, SqliteDriverNotFoundException;

    boolean tableExists(String name) throws SQLException, SqliteDriverNotFoundException;
}
