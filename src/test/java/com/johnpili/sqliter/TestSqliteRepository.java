package com.johnpili.sqliter;

import com.johnpili.sqliter.constants.TestSqlStatements;
import com.johnpili.sqliter.exceptions.SqliteDriverNotFoundException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSqliteRepository {
    private SqliteRepository sqliteRepository;
    private static final String testDbFilename = "test.db";

    private Movie getMovie(long id) throws SQLException, SqliteDriverNotFoundException {
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, id);
        return this.sqliteRepository.getSingle(TestSqlStatements.GET_MOVIE_BY_ID, parameters, MovieAssembler.getInstance());
    }

    @BeforeEach
    void setUp() {
        try {
            Class.forName("org.sqlite.JDBC");
            new File(testDbFilename);
            SqliteConfig sqliteConfig = new SqliteConfig();
            sqliteConfig.setDbLocation(testDbFilename);

            sqliteRepository = new SqliteRepository(sqliteConfig);
            this.sqliteRepository.createTable(TestSqlStatements.CREATE_TEST_TABLE);
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testGetList() {
        try {
            List<Movie> movies = this.sqliteRepository.getList(TestSqlStatements.GET_ALL_MOVIES, MovieAssembler.getInstance());
            Assertions.assertEquals(10, movies.size());
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testGetSingle() {
        try {
            Map<Integer, Object> parameters = new HashMap<>();
            parameters.put(1, 5);
            Movie movie = this.sqliteRepository.getSingle(TestSqlStatements.GET_MOVIE_BY_ID, parameters, MovieAssembler.getInstance());
            Assertions.assertNotNull(movie);
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testUpdate() {
        try {
            Map<Integer, Object> parameters = new HashMap<>();
            parameters.put(1, "Movie Updated");
            parameters.put(2, 5);
            Assertions.assertEquals(1, this.sqliteRepository.update(TestSqlStatements.UPDATE_MOVIE, parameters));

            Movie movie = getMovie(5);
            Assertions.assertNotNull(movie);
            Assertions.assertEquals("Movie Updated", movie.getTitle());

        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testInsert() {
        try {
            Map<Integer, Object> parameters = new HashMap<>();
            parameters.put(1, "New Movie");
            int newId = this.sqliteRepository.insert(TestSqlStatements.INSERT_MOVIE, parameters);
            Movie movie = getMovie(newId);
            Assertions.assertNotNull(movie);
            Assertions.assertEquals("New Movie", movie.getTitle());
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testNullGetSingle() {
        try {
            Assertions.assertNull(getMovie(1000));
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testDelete() {
        try {
            Map<Integer, Object> parameters = new HashMap<>();
            parameters.put(1, "Movie for test delete");
            int newId = this.sqliteRepository.insert(TestSqlStatements.INSERT_MOVIE, parameters);
            Assertions.assertNotNull(getMovie(newId));

            parameters = new HashMap<>();
            parameters.put(1, newId);
            Assertions.assertNotEquals(0, this.sqliteRepository.delete(TestSqlStatements.DELETE_MOVIE, parameters));
            Assertions.assertNull(getMovie(newId));
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testGetTables() {
        try {
            List<String> tableNames = this.sqliteRepository.getTables();
            Assertions.assertNotEquals(0, tableNames.size());
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    public void testTableExists() {
        try {
            Assertions.assertTrue(this.sqliteRepository.tableExists("movie"));
            Assertions.assertFalse(this.sqliteRepository.tableExists("imovie_"));
        } catch (Exception exception) {
            Assertions.fail(exception);
        }
    }

    @AfterAll
    static void afterAll() {
        File file = new File(testDbFilename);
        file.delete();
    }
}
