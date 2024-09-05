# Custom Redisson Cache Manager

## Overview

`CustomRedissonCacheManager` is a custom Spring Cache Manager implementation that integrates with Redisson’s `RBucket` cache. This project provides a tailored solution for managing caches using Redisson while maintaining backward compatibility with the default Spring Cache Manager.

## Features

- **Custom Cache Manager**: Implements a cache manager using Redisson’s `RBucket`.
- **Backward Compatibility**: Compatible with existing Spring applications using the default cache manager.
- **Flexible Configuration**: Easily configurable to suit various caching needs.
- **Enhanced Cache TTL Configuration**: Support randomized cache TTL configuration.

## Prerequisites

- **Java 8+**: The project is compatible with Java 8 and above.
- **Spring Framework**: Compatible with Spring Framework 5.x and above.
- **Redisson**: Requires Redisson 3.x or later.

## Required Dependencies

Add the following dependencies to your `pom.xml` for Maven or `build.gradle` for Gradle:

**Maven:**
```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.20.1</version>
</dependency>
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-data-30</artifactId>
    <version>3.20.1</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.x.x</version>
</dependency>
```

## Customization

`CustomRedissonCacheManager` can be customized further based on your requirements.

## Contributing

Contributions are welcome! Please follow the standard GitHub workflow for contributing:

- Fork the repository.
- Create a new branch.
- Make your changes.
- Submit a pull request with a clear description of your changes.

## Contact

For any questions or issues, please contact grahadhuita@gmail.com.
