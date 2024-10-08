package com.job;

import com.conf.JobConfiguration;
import com.mapping.FootballMatchStatsMapper;
import com.model.event.FootballMatchEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mapping.FootballMatchEventDeserializer;
import com.mapping.FootballMatchStatTableMapper;
import com.model.table.FootballMatchStats;
import com.mapping.MatchIdKeySelector;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.flink.CatalogLoader;
import org.apache.iceberg.flink.TableLoader;
import org.apache.iceberg.flink.sink.FlinkSink;
import org.apache.flink.configuration.StateBackendOptions;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FootballMatchStatsTableSinkJob extends TableSinkJob<FootballMatchEvent, FootballMatchStats, RowData> {

    private static final Logger logger = LoggerFactory.getLogger(FootballMatchStatsTableSinkJob.class);
    final private JobConfiguration jobConfiguration;

    protected FootballMatchStatsTableSinkJob(String jobName, JobConfiguration jobConfiguration) {
        super(jobName);
        this.jobConfiguration = jobConfiguration;
    }

    @Override
    protected StreamExecutionEnvironment initStreamExecutionEnv() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment()
                                                                   .enableCheckpointing(10000)
                                                                   .setParallelism(1)
                                                                   .setMaxParallelism(1);

        env.enableCheckpointing(10000); // 10 seconds
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        env.getCheckpointConfig().enableUnalignedCheckpoints();
        Configuration config = new Configuration();
        config.set(StateBackendOptions.STATE_BACKEND, "hashmap");
        config.set(CheckpointingOptions.CHECKPOINT_STORAGE, "filesystem");
        config.set(CheckpointingOptions.CHECKPOINTS_DIRECTORY, jobConfiguration.getFlink().getCheckpointLocation());
        env.configure(config);
        return env;
    }

    @Override
    void initCatalog() {
        StreamTableEnvironment streamTableEnv = StreamTableEnvironment.create(env);
        streamTableEnv.executeSql(jobConfiguration.getHive().getCatalog().getDdl());
        streamTableEnv.useCatalog(jobConfiguration.getHive().getCatalog().getName());
        streamTableEnv.executeSql(jobConfiguration.getHive().getDatabase().getDdl());
        streamTableEnv.executeSql(jobConfiguration.getHive().getTable().getDdl());
        logger.info("Hive Catalog Initialized");
    }

    @Override
    public KafkaSource<FootballMatchEvent> loadKafkaSource() {
        return KafkaSource.<FootballMatchEvent>builder()
                          .setBootstrapServers(jobConfiguration.getKafka().getBroker())
                          .setTopics(jobConfiguration.getKafka().getTopicName())
                          .setGroupId(jobConfiguration.getKafka().getConsumerGroupName())
                          .setDeserializer(new FootballMatchEventDeserializer())
                          .setStartingOffsets(OffsetsInitializer.earliest())
                          .build();
    }

    @Override
    public DataStream<FootballMatchStats> map(KafkaSource<FootballMatchEvent> kafkaSource) {
        WatermarkStrategy<FootballMatchEvent> watermarkStrategy = WatermarkStrategy.<FootballMatchEvent>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                                                                                   .withTimestampAssigner((event, timestamp) -> event.getEventTime());

        return env.fromSource(kafkaSource, watermarkStrategy, jobConfiguration.getKafka().getSourceName())
                  .keyBy(new MatchIdKeySelector(), TypeInformation.of(String.class))
                  .map(new FootballMatchStatsMapper());
    }

    @Override
    public MapFunction<FootballMatchStats, RowData> tableMapper() {
        return new FootballMatchStatTableMapper();
    }

    private Map<String, String> getHiveProperties() {
        Map<String, String> hiveProperties = new HashMap<>();
        hiveProperties.put("type", "iceberg");
        hiveProperties.put("connector", "iceberg");
        hiveProperties.put("catalog-name", jobConfiguration.getHive().getCatalog().getName());
        hiveProperties.put("catalog-type", "hive");
        hiveProperties.put("catalog-database", jobConfiguration.getHive().getDatabase().getName());
        hiveProperties.put("catalog-table", jobConfiguration.getHive().getTable().getName());
        hiveProperties.put("uri", jobConfiguration.getHive().getMetastore());
        return hiveProperties;
    }

    @Override
    public void write(DataStream<RowData> dataStream) {
        String qualifiedTableName = jobConfiguration.getHive()
                                                    .getDatabase()
                                                    .getName() + "." + jobConfiguration.getHive().getTable().getName();
        CatalogLoader catalogLoader = CatalogLoader.hive(jobConfiguration.getHive()
                                                                         .getCatalog()
                                                                         .getName(), new org.apache.hadoop.conf.Configuration(), getHiveProperties());
        TableLoader tableLoader = TableLoader.fromCatalog(catalogLoader, TableIdentifier.parse(qualifiedTableName));
        FlinkSink.forRowData(dataStream).tableLoader(tableLoader).upsert(true).append();
    }

    private static JobConfiguration load(String fileName) throws IOException {
        return new ObjectMapper(new YAMLFactory()).readValue(new FileInputStream(fileName), JobConfiguration.class);
    }

    public static void main(String[] args) {
        ParameterTool parameters = ParameterTool.fromArgs(args);
        String configFileName = parameters.getRequired("job-config");
        try {
            FootballMatchStatsTableSinkJob tableSinkJob = new FootballMatchStatsTableSinkJob("flink-iceberg-sink-job", load(configFileName));
            tableSinkJob.run();
        } catch (Exception e) {
            logger.error("Exception Occurred! Message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
