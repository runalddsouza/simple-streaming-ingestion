package com.conf;


public class Flink {

    private String checkpointLocation;

    /**
     * No args constructor for use in serialization
     */
    public Flink() {
    }

    public Flink(String checkpointLocation) {
        super();
        this.checkpointLocation = checkpointLocation;
    }

    public String getCheckpointLocation() {
        return checkpointLocation;
    }

    public void setCheckpointLocation(String checkpointLocation) {
        this.checkpointLocation = checkpointLocation;
    }

    public Flink withCheckpointLocation(String checkpointLocation) {
        this.checkpointLocation = checkpointLocation;
        return this;
    }

}