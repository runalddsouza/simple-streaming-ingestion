package com.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"flink", "hdfs", "hive", "kafka"})
public class JobConfiguration {

    @JsonProperty("flink")
    private Flink flink;
    @JsonProperty("hdfs")
    private Hdfs hdfs;
    @JsonProperty("hive")
    private Hive hive;
    @JsonProperty("kafka")
    private Kafka kafka;

    /**
     * No args constructor for use in serialization
     */
    public JobConfiguration() {
    }

    public JobConfiguration(Flink flink, Hdfs hdfs, Hive hive, Kafka kafka) {
        super();
        this.flink = flink;
        this.hdfs = hdfs;
        this.hive = hive;
        this.kafka = kafka;
    }

    @JsonProperty("flink")
    public Flink getFlink() {
        return flink;
    }

    @JsonProperty("flink")
    public void setFlink(Flink flink) {
        this.flink = flink;
    }

    public JobConfiguration withFlink(Flink flink) {
        this.flink = flink;
        return this;
    }

    @JsonProperty("hdfs")
    public Hdfs getHdfs() {
        return hdfs;
    }

    @JsonProperty("hdfs")
    public void setHdfs(Hdfs hdfs) {
        this.hdfs = hdfs;
    }

    public JobConfiguration withHdfs(Hdfs hdfs) {
        this.hdfs = hdfs;
        return this;
    }

    @JsonProperty("hive")
    public Hive getHive() {
        return hive;
    }

    @JsonProperty("hive")
    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public JobConfiguration withHive(Hive hive) {
        this.hive = hive;
        return this;
    }

    @JsonProperty("kafka")
    public Kafka getKafka() {
        return kafka;
    }

    @JsonProperty("kafka")
    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    public JobConfiguration withKafka(Kafka kafka) {
        this.kafka = kafka;
        return this;
    }
}
