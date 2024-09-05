package com.example.app.cache.manager;

import com.example.app.cache.codec.RedissonCacheCompressionCodec;
import com.example.app.cache.config.RedissonProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonUtils {

  private final RedissonProperties redissonProperties;

  private static final String REDIS_PROTOCOL = "redis://";
  private static final String REDIS_PORT = ":6379";

  /**
   * Get Redisson Connection Factory
   *
   * @param host Redis Host
   * @return Redisson Connection Factory
   */
  public RedissonConnectionFactory getRedissonConnectionFactory(String host) {
    return new RedissonConnectionFactory(getRedissonClient(host));
  }

  /**
   * Get Redisson Connection Factory with Compressed Codec
   *
   * @param host Redis Host
   * @return Redisson Connection Factory
   */
  public RedissonConnectionFactory getCompressedRedissonConnectionFactory(String host) {
    return new RedissonConnectionFactory(getRedissonClient(host, true));
  }

  /**
   * Get Redisson Client
   *
   * @param host Redis Host
   * @return Redisson Client
   */
  public RedissonClient getRedissonClient(String host) {
    return getRedissonClient(host, false);
  }

  /**
   * Get Redisson Client with Compressed Codec
   *
   * @param host Redis Host
   * @return Redisson Client
   */
  public RedissonClient getCompressedRedissonClient(String host) {
    return getRedissonClient(host, true);
  }

  private RedissonClient getRedissonClient(String host, boolean useCacheCompression) {
    Config config = getRedissonConfig(host);
    if (useCacheCompression) {
      config.setCodec(new RedissonCacheCompressionCodec());
    } else {
      // default serialization for spring redis, to maintain backward compatibility
      config.setCodec(new SerializationCodec());
    }
    return Redisson.create(config);
  }

  private Config getRedissonConfig(String host) {
    // Create Redisson Client Configuration
    Config config = new Config();
    config.useClusterServers()
        .addNodeAddress(REDIS_PROTOCOL + host + REDIS_PORT)
        .setReadMode(ReadMode.MASTER_SLAVE)
        .setLoadBalancer(new RoundRobinLoadBalancer())
        .setTimeout(redissonProperties.getTimeout())
        .setConnectTimeout(redissonProperties.getConnectTimeout())
        .setRetryAttempts(redissonProperties.getRetryAttempts())
        .setPingConnectionInterval(redissonProperties.getPingConnectionInterval());
    return config;
  }
}
