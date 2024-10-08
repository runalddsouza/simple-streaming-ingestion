package com.conf;

public class Hdfs {

    private String namenode;

    /**
     * No args constructor for use in serialization
     */
    public Hdfs() {
    }

    public Hdfs(String namenode) {
        super();
        this.namenode = namenode;
    }

    public String getNamenode() {
        return namenode;
    }

    public void setNamenode(String namenode) {
        this.namenode = namenode;
    }

    public Hdfs withNamenode(String namenode) {
        this.namenode = namenode;
        return this;
    }

}