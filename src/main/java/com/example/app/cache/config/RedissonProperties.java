package com.example.app.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {
  private int timeout = 5000;
  private int connectTimeout = 5000;
  private int retryAttempts = 1;
  private int pingConnectionInterval = 30000;
}
