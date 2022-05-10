#!/bin/bash
# Requires installed graalvm and gradle

gradle clean build test installDist distZip

JARS_DIR=build/install/web-server-demo/lib/
CLASS_PATH=""

for JAR in $(ls -1 $JARS_DIR); do
  CLASS_PATH=${JARS_DIR}${JAR}:${CLASS_PATH}
done

echo "CLASS PATH: ${CLASS_PATH}"

native-image --no-fallback \
  -cp ${CLASS_PATH} one.microproject.webserver.Main

rm one.microproject.webserver.main.build_artifacts.txt
mv one.microproject.webserver.main build/distributions/web-server-demo

# run binary proxy-server
# build/distributions/proxy-server src/main/resources/web-server-config.json
