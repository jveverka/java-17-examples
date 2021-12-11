#!/bin/bash

export JAVA_HOME=/opt/proxy-server/jdk-17.0.1+12
export PATH=$JAAVA_HOME/bin:$PATH

LOG_FILE=/opt/proxy-server/proxy-server.log
CONFIG=/opt/proxy-server/proxy-server-config.json

mv $LOG_FILE $LOG_FILE.old

/opt/proxy-server/proxy-server-1.0.0/bin/proxy-server $CONFIG > $LOG_FILE 2>&1

