package com.johnpili.crud.sqlite;

/**
 * @author John Pili
 */
public class SqliteConfig {
    private String dbLocation;

    public SqliteConfig() {
        this.dbLocation = "jdbc:sqlite:";
    }

    public SqliteConfig(String dbLocation) {
        this.dbLocation = dbLocation;
    }

    public String getDbLocation() {
        if (this.dbLocation.startsWith("jdbc:sqlite:")) {
            return this.dbLocation;
        }
        return "jdbc:sqlite:" + this.dbLocation;
    }

    public void setDbLocation(String dbLocation) {
        this.dbLocation = dbLocation;
    }
}
