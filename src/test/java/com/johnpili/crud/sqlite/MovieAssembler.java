package com.johnpili.crud.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieAssembler implements ISqliteObjectAssembler<Movie> {
    private static MovieAssembler movieAssembler = null;

    private MovieAssembler() {}

    public static MovieAssembler getInstance() {
        if(movieAssembler == null) {
            movieAssembler = new MovieAssembler();
        }
        return movieAssembler;
    }

    @Override
    public Movie assemble(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getLong(1));
        movie.setTitle(resultSet.getString(2));
        return movie;
    }
}
