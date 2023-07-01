package com.johnpili.sqliter.exceptions;

public class SqliteDriverNotFoundException extends Exception {
    public SqliteDriverNotFoundException() {
        super();
    }

    public SqliteDriverNotFoundException(String message) {
        super(message);
    }
}
