[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java15](https://img.shields.io/badge/java-15-blue)](https://img.shields.io/badge/java-15-blue)
[![Gradle](https://img.shields.io/badge/gradle-v6.5-blue)](https://img.shields.io/badge/gradle-v6.5-blue)
![Build and Test](https://github.com/jveverka/java-17-examples/workflows/Build%20and%20Test/badge.svg)

# Java 17 examples

*This project is __WIP__* ! Stay tuned.

This project aggregates examples of new Java and JVM features between versions 12 and 17 
as well as practical java examples tailored for Java 17**.

** JDK17 is not available yet. The JDK15 is used until JDK17 is available.

### Environment setup
Make sure following software is installed on your PC.
* [OpenJDK 15](https://adoptopenjdk.net/releases.html?variant=openjdk15&jvmVariant=hotspot) or later.
* [Gradle 6.5](https://gradle.org/install/) or later
* [docker.io 19.x](https://www.docker.com/) or later 

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
  * [JEP 355, 368, 378: Text Blocks](https://openjdk.java.net/jeps/378)
  * [JEP 305: Pattern Matching for instanceof](https://openjdk.java.net/jeps/305)
  * [__JEP 358: Helpful NullPointerExceptions__](jep-examples/jep-358_helpful-npe)
  * [JEP 373: Reimplement the Legacy DatagramSocket API](https://openjdk.java.net/jeps/373)
  * [JEP 370, 383: Foreign-Memory Access API](https://openjdk.java.net/jeps/383)
  * [JEP 339: Edwards-Curve Digital Signature Algorithm (EdDSA)](https://openjdk.java.net/jeps/339)
  * [JEP 360: Sealed Classes (Preview)](https://openjdk.java.net/jeps/360)
  * [JEP 371: Hidden Classes](https://openjdk.java.net/jeps/371)

### JDK12 - JDK17 Features
* JDK12 [Feature list](https://openjdk.java.net/projects/jdk/12/)
* JDK13 [Feature list](https://openjdk.java.net/projects/jdk/13/)
* JDK14 [Feature list](https://openjdk.java.net/projects/jdk/14/)
* JDK15 [Feature list](https://openjdk.java.net/projects/jdk/15/)
* JDK16 [Feature list](https://openjdk.java.net/projects/jdk/16/)
* JDK17 [Feature list](https://openjdk.java.net/projects/jdk/17/)

### References
[Java 11 examples](https://github.com/jveverka/java-11-examples) 

_Enjoy !_ 
