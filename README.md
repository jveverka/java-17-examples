[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java15](https://img.shields.io/badge/java-15-blue)](https://img.shields.io/badge/java-15-blue)
[![Gradle](https://img.shields.io/badge/gradle-v6.5-blue)](https://img.shields.io/badge/gradle-v6.5-blue)
![Build and Test](https://github.com/jveverka/java-17-examples/workflows/Build%20and%20Test/badge.svg)

# Java 17 examples

*This project is __WIP__* ! Stay tuned.

This project aggregates examples of new Java and JVM features between versions 12 and 17 
as well as practical java examples tailored for Java 17**.

** JDK17 is not available yet. The JDK16 is used until JDK17 is available.

### Environment setup
Make sure following software is installed on your PC.
* [OpenJDK 16](https://adoptopenjdk.net/releases.html?variant=openjdk15&jvmVariant=hotspot) or later.
* [Gradle 7.0](https://gradle.org/install/) or later
* [docker.io 20.x](https://www.docker.com/) or later 

Please check [system requirements](docs/system-requirements.md) before. 

### Compile & Test
Most examples are build by top-level gradle project.
```
gradle clean build test
```

### Examples
* Most interesting [JEPs](http://openjdk.java.net/jeps/1) implemented in JDK12 - JDK17
  * [__JEP 359, 384: Records__](jep-examples/jep-384_records)
  * [JEP 325, 354, 361: Switch Expressions](https://openjdk.java.net/jeps/361)
  * [JEP 353: Reimplement the Legacy Socket API](https://openjdk.java.net/jeps/353)
  * [__JEP 355, 368, 378: Text Blocks__](jep-examples/jep-378_text-blocks)
  * [JEP 305: Pattern Matching for instanceof](https://openjdk.java.net/jeps/305)
  * [__JEP 358: Helpful NullPointerExceptions__](jep-examples/jep-358_helpful-npe)
  * [JEP 373: Reimplement the Legacy DatagramSocket API](https://openjdk.java.net/jeps/373)
  * [JEP 370, 383: Foreign-Memory Access API](https://openjdk.java.net/jeps/383)
  * [JEP 339: Edwards-Curve Digital Signature Algorithm (EdDSA)](https://openjdk.java.net/jeps/339)
  * [JEP 360: Sealed Classes (Preview)](https://openjdk.java.net/jeps/360)
  * [JEP 371: Hidden Classes](https://openjdk.java.net/jeps/371)

### JDK12 - JDK17 Features
* JDK12 [2019-03-19] [Feature list](https://openjdk.java.net/projects/jdk/12/)
* JDK13 [2019-09-17] [Feature list](https://openjdk.java.net/projects/jdk/13/)
* JDK14 [2020-03-17] [Feature list](https://openjdk.java.net/projects/jdk/14/)
* JDK15 [2020-09-15] [Feature list](https://openjdk.java.net/projects/jdk/15/)
* JDK16 [2021-03-16] [Feature list](https://openjdk.java.net/projects/jdk/16/)
* JDK17 [2021-09-14] [Feature list](https://openjdk.java.net/projects/jdk/17/)

### References
[Java 11 examples](https://github.com/jveverka/java-11-examples) 
[A peek into Java 17](https://blogs.oracle.com/javamagazine/java-runtime-encapsulation-internals)

_Enjoy !_ 
