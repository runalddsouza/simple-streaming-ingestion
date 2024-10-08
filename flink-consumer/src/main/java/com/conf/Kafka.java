package com.conf;

public class Kafka {

    private String sourceName;
    private String broker;
    private String topicName;
    private String consumerGroupName;

    /**
     * No args constructor for use in serialization
     */
    public Kafka() {
    }

    public Kafka(String sourceName, String broker, String topicName, String consumerGroupName) {
        super();
        this.sourceName = sourceName;
        this.broker = broker;
        this.topicName = topicName;
        this.consumerGroupName = consumerGroupName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Kafka withSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public Kafka withBroker(String broker) {
        this.broker = broker;
        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Kafka withTopicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public Kafka withConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
        return this;
    }

}