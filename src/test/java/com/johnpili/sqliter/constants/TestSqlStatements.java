package com.johnpili.sqliter.constants;

public class TestSqlStatements {
    public static final String CREATE_TEST_TABLE = "DROP TABLE IF EXISTS movie; " +
            "CREATE TABLE movie ( " +
            "id integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT); " +
            "BEGIN; " +
            "INSERT INTO movie(title) VALUES ('Movie 1'); " +
            "INSERT INTO movie(title) VALUES ('Movie 2'); " +
            "INSERT INTO movie(title) VALUES ('Movie 3'); " +
            "INSERT INTO movie(title) VALUES ('Movie 4'); " +
            "INSERT INTO movie(title) VALUES ('Movie 5'); " +
            "INSERT INTO movie(title) VALUES ('Movie 6'); " +
            "INSERT INTO movie(title) VALUES ('Movie 7'); " +
            "INSERT INTO movie(title) VALUES ('Movie 8'); " +
            "INSERT INTO movie(title) VALUES ('Movie 9'); " +
            "INSERT INTO movie(title) VALUES ('Movie 10'); " +
            "COMMIT; ";

    public static final String GET_ALL_MOVIES = "SELECT * FROM movie ORDER BY title";

    public static final String GET_MOVIE_BY_ID = "SELECT * FROM movie WHERE id = ?";

    public static final String UPDATE_MOVIE = "UPDATE movie SET title = ? WHERE id = ?";

    public static final String INSERT_MOVIE = "INSERT INTO movie(title) VALUES(?)";

    public static final String DELETE_MOVIE = "DELETE FROM movie WHERE id = ?";
}
