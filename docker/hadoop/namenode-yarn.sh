#!/bin/bash

if [ ! -d "/home/hadoop/hdfs/namenode" ]; then
    hdfs namenode -format
fi

echo "Starting Namenode"
hdfs namenode &
echo "Starting Resource Manager"
yarn resourcemanager
