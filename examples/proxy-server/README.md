# TCP Proxy Server

Forward TCP or UDP connections from bind address and port to another server.
See [configuration example](src/test/resources/proxy-server-config.json).

## Requirements
* Java 17
* Gradle 7.2 or later

## Build
```
gradle clean build test installDist distZip
```

## Run
```
./build/install/proxy-server/bin/proxy-server src/main/resources/proxy-server-config.json
```

## Test
```
ssh user@127.0.0.1 -p 10111
nc -z -v 127.0.0.1 10111
nc -z -v -u 127.0.0.1 20222
```
