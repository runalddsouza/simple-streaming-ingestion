package com.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;

public interface KafkaEventTransformer<E, T> {
    KafkaSource<E> loadKafkaSource();

    DataStream<T> map(KafkaSource<E> kafkaSource);

    default DataStream<T> transform() {
        return map(loadKafkaSource());
    }
}
