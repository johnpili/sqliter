package com.johnpili.crud.sqlite;

import com.johnpili.crud.sqlite.constants.SqlStatements;
import com.johnpili.crud.sqlite.exceptions.SqliteDriverNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.johnpili.crud.sqlite.constants.SqlStatements.LAST_INSERT_ID;

/**
 * @author John Pili
 */
public class SqliteRepository implements ISqliteRepository {
    private final SqliteConfig sqliteConfig;

    public SqliteRepository(SqliteConfig sqliteConfig) {
        this.sqliteConfig = sqliteConfig;
    }

    public SqliteRepository(String dbLocation) {
        this.sqliteConfig = new SqliteConfig(dbLocation);
    }

    @Override
    public Connection getConnection() throws SQLException, SqliteDriverNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(sqliteConfig.getDbLocation());
            try (PreparedStatement preparedStatement = connection.prepareStatement("PRAGMA foreign_keys=ON")) {
                preparedStatement.execute();
            }
            return connection;
        } catch (ClassNotFoundException classNotFoundException) {
            throw new SqliteDriverNotFoundException("Cannot load org.sqlite.JDBC driver");
        }
    }

    @Override
    public Connection getConnection(boolean pragmaFk) throws SQLException, SqliteDriverNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(sqliteConfig.getDbLocation());
            try (PreparedStatement preparedStatement = connection.prepareStatement((pragmaFk ? "PRAGMA foreign_keys=true" : "PRAGMA foreign_keys=false"))) {
                preparedStatement.execute();
            }
            return connection;
        } catch (ClassNotFoundException classNotFoundException) {
            throw new SqliteDriverNotFoundException("Cannot load org.sqlite.JDBC driver");
        }
    }

    @Override
    public int insert(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sanitizeSqlString(sql))) {
                if (parameters != null) {
                    injectParameterToPreparedStatement(parameters, preparedStatement);
                }
                preparedStatement.executeUpdate();
                return getLastInsertedId(connection);
            }
        }
    }

    @Override
    public int update(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException {
        return execute(sql, parameters);
    }

    @Override
    public int delete(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException {
        return execute(sql, parameters);
    }

    @Override
    public <T> T getSingle(String sql, ISqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet != null && resultSet.next()) {
                    return (T) sqliteObjectAssembler.assemble(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public <T> T getSingle(String sql, Map<Integer, Object> parameters, ISqliteObjectAssembler sqliteObjectAssembler)
            throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sanitizeSqlString(sql))) {
                if (parameters != null) {
                    injectParameterToPreparedStatement(parameters, preparedStatement);
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet != null && resultSet.next()) {
                    return (T) sqliteObjectAssembler.assemble(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public <T> List<T> getList(String sql, ISqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                return executeListQuery(resultSet, sqliteObjectAssembler);
            }
        }
    }

    @Override
    public <T> List<T> getList(String sql, Map<Integer, Object> parameters,
                               ISqliteObjectAssembler sqliteObjectAssembler) throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sanitizeSqlString(sql))) {
                if (parameters != null) {
                    injectParameterToPreparedStatement(parameters, preparedStatement);
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                return executeListQuery(resultSet, sqliteObjectAssembler);
            }
        }
    }

    @Override
    public void createTable(String sql) throws SQLException, SqliteDriverNotFoundException {
        tableSqlExecutor(sql);
    }

    @Override
    public void alterTable(String sql) throws SQLException, SqliteDriverNotFoundException {
        tableSqlExecutor(sql);
    }

    @Override
    public void dropTable(String sql) throws SQLException, SqliteDriverNotFoundException {
        tableSqlExecutor(sql);
    }

    @Override
    public List<String> getTables() throws SQLException, SqliteDriverNotFoundException {
        return getList(SqlStatements.GET_TABLES, new ISqliteObjectAssembler() {
            @Override
            public Object assemble(ResultSet resultSet) throws SQLException {
                return resultSet.getString(1);
            }
        });
    }

    @Override
    public boolean tableExists(String name) throws SQLException, SqliteDriverNotFoundException {
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, name);
        String result = getSingle(SqlStatements.GET_TABLE_BY_NAME, parameters, new ISqliteObjectAssembler() {
            @Override
            public Object assemble(ResultSet resultSet) throws SQLException {
                return resultSet.getString("name");
            }
        });
        return (result != null && result.equals(name));
    }

    /**
     * This method handles DDL
     *
     * @param sql
     * @throws SQLException
     */
    private void tableSqlExecutor(String sql) throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection(false)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
    }

    /**
     * This injects hash map into the prepared statement
     *
     * @param parameters
     * @param preparedStatement
     * @throws SQLException
     */
    private void injectParameterToPreparedStatement(Map<Integer, Object> parameters, PreparedStatement preparedStatement) throws SQLException {
        if (parameters != null && preparedStatement != null) {
            for (Map.Entry entry : parameters.entrySet()) {
                preparedStatement.setObject((int) entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * A bit of rainbow and sugar string sanitation
     *
     * @param sql
     * @return
     */
    private String sanitizeSqlString(String sql) {
        if (sql != null) {
            return sql.trim();
        }
        return "";
    }

    /**
     * This will retrieve the late generated ID from SQLite
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    private int getLastInsertedId(Connection connection) throws SQLException {
        if (connection != null) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(LAST_INSERT_ID);
            if (resultSet != null) {
                return resultSet.getInt(1);
            }
        }
        return -1;
    }

    /**
     * This method handles the execution of prepared statements
     * returns the number of affected records
     *
     * @param sql
     * @param parameters
     * @return
     * @throws SQLException
     * @throws SqliteDriverNotFoundException
     */
    private int execute(String sql, Map<Integer, Object> parameters) throws SQLException, SqliteDriverNotFoundException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sanitizeSqlString(sql))) {
                if (parameters != null) {
                    injectParameterToPreparedStatement(parameters, preparedStatement);
                }
                return preparedStatement.executeUpdate();
            }
        }
    }

    /**
     * This method handles the execution of prepared statements
     * returns the result as a list
     *
     * @param resultSet
     * @param sqliteObjectAssembler
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> executeListQuery(ResultSet resultSet, ISqliteObjectAssembler sqliteObjectAssembler) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet != null && resultSet.next()) {
            results.add((T) sqliteObjectAssembler.assemble(resultSet));
        }
        return results;
    }

}
