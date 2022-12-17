# Compare Files
Recursively compare two directories for file and data content.

### Requirements
* Java 17
* Gradle 7.3 or later

### Build
```
gradle clean build test installDist distZip
```

### Run 
```shell
./build/install/files-compare-demo/bin/files-compare-demo <src-dir> <dst-dir>
```