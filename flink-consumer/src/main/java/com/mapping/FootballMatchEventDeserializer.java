package com.mapping;

import com.model.event.FootballMatchEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class FootballMatchEventDeserializer implements KafkaRecordDeserializationSchema<FootballMatchEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<FootballMatchEvent> collector) throws IOException {
        FootballMatchEvent footballMatchStats = objectMapper.readValue(consumerRecord.value(), FootballMatchEvent.class);
        collector.collect(footballMatchStats);
    }

    @Override
    public TypeInformation<FootballMatchEvent> getProducedType() {
        return TypeInformation.of(FootballMatchEvent.class);
    }
}
