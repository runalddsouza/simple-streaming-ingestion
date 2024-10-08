package com.conf;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Hive {

    @JsonProperty("metastore")
    private String metastore;

    @JsonProperty("catalog")
    private Catalog catalog;
    @JsonProperty("database")
    private Database database;
    @JsonProperty("table")
    private Table table;

    @JsonProperty("catalog")
    public Catalog getCatalog() {
        return catalog;
    }

    @JsonProperty("catalog")
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    @JsonProperty("database")
    public Database getDatabase() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabase(Database database) {
        this.database = database;
    }

    @JsonProperty("table")
    public Table getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(Table table) {
        this.table = table;
    }

    @JsonProperty("metastore")
    public String getMetastore() {
        return metastore;
    }

    @JsonProperty("metastore")
    public void setMetastore(String metastore) {
        this.metastore = metastore;
    }

}