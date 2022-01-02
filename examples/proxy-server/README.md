# TCP Proxy Server

This proxy server forwards TCP or UDP connections from bind address and port to another server.
See [configuration example](src/test/resources/proxy-server-config.json).

### Requirements
* Java 17
* Gradle 7.3 or later

### Build
```
gradle clean build test installDist distZip
```

### Run and Test
```
./build/install/proxy-server/bin/proxy-server src/main/resources/proxy-server-config.json
```
```
ssh user@127.0.0.1 -p 10111
nc -z -v 127.0.0.1 10111
nc -z -v -u 127.0.0.1 20222
```

## Native Build
It is possible to create native binary build of ``proxy-server`` using Graal's ``native-image`` tool.

### Requirements
* Graal JVM 17
* Gradle 7.3 or later

### Build
```
./build-native.sh
```

### Run
```
build/distributions/proxy-server src/main/resources/proxy-server-config.json
```