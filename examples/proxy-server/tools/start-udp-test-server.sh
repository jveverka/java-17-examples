#!/bin/bash

CP_ROOT=../build/install/proxy-server/lib

CLASSPATH=""
for JAR_FILE in $(ls -1 $CP_ROOT); do
  CLASSPATH="${CLASSPATH}:${CP_ROOT}/${JAR_FILE}"
done

java -cp ${CLASSPATH} one.microproject.proxyserver.test.UDPServerMain $1 $2
