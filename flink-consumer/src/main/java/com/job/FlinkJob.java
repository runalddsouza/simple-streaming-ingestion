package com.job;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

abstract class FlinkJob {

    protected String jobName;

    protected FlinkJob(String jobName) {
        this.jobName = jobName;
    }

    protected StreamExecutionEnvironment env;

    protected abstract StreamExecutionEnvironment initStreamExecutionEnv();

    protected abstract void build();

    final void run() {
        this.env = initStreamExecutionEnv();
        build();
        execute();
    }

    final protected void execute() {
        try {
            env.execute(jobName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
