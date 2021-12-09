# TCP Proxy Server

Forward TCP connection from bind address and port to another server.
See [configuration example](tcp-proxy-config.json).

## Requirements
* Java 17
* Gradle 7.2 or later

## Build
```
gradle clean build test installDist distZip
```

## Run
```
./build/install/tcp-proxy/bin/tcp-proxy tcp-proxy-config.json
```