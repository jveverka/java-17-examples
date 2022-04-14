#!/bin/bash
# Requires installed graalvm and gradle

gradle clean build test installDist distZip

JARS_DIR=build/install/proxy-server/lib/
CLASS_PATH=""

for JAR in $(ls -1 $JARS_DIR); do
  CLASS_PATH=${JARS_DIR}${JAR}:${CLASS_PATH}
done

echo "CLASS PATH: ${CLASS_PATH}"

native-image --no-fallback \
  -H:ReflectionConfigurationFiles=reflectconfig.json \
  -cp ${CLASS_PATH} one.microproject.proxyserver.Main

rm one.microproject.proxyserver.main.build_artifacts.txt
mv one.microproject.proxyserver.main build/distributions/proxy-server

# run binary proxy-server
# build/distributions/proxy-server src/main/resources/proxy-server-config.json
