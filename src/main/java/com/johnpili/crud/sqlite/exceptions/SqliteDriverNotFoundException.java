package com.johnpili.crud.sqlite.exceptions;

public class SqliteDriverNotFoundException extends Exception {
    public SqliteDriverNotFoundException() {
        super();
    }

    public SqliteDriverNotFoundException(String message) {
        super(message);
    }
}
