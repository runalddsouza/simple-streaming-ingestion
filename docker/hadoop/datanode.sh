#!/bin/bash

echo "Starting Datanode"
hdfs datanode &

echo "Starting Node Manager"
yarn nodemanager

