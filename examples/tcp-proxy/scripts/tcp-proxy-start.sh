#!/bin/bash

export JAVA_HOME=/opt/tcp-proxy/jdk-17.0.1+12
export PATH=$JAAVA_HOME/bin:$PATH

LOG_FILE=/opt/tcp-proxy/tcp-proxy.log
CONFIG=/opt/tcp-proxy/tcp-proxy-config.json

mv $LOG_FILE $LOG_FILE.old

/opt/tcp-proxy/tcp-proxy-1.0.0/bin/tcp-proxy $CONFIG > $LOG_FILE 2>&1

