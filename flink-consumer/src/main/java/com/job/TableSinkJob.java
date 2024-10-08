package com.job;

import com.transform.KafkaEventTransformer;

abstract class TableSinkJob<E, T, D> extends FlinkJob implements KafkaEventTransformer<E, T>, TableWriter<T,D> {
    protected TableSinkJob(String jobName) {
        super(jobName);
    }

    abstract void initCatalog();

    @Override
    protected void build() {
        initCatalog();
        executeWrite(transform());
    }
}
