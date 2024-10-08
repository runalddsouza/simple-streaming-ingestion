package com.job;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;

interface TableWriter<T, D> {

    MapFunction<T, D> tableMapper();

    void write(DataStream<D> dataStream);

    default void executeWrite(DataStream<T> dataStream) {
        write(dataStream.map(tableMapper()));
    }
}
