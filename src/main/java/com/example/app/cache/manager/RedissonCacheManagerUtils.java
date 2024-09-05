package com.example.app.cache.manager;

import com.example.app.cache.config.CacheTTLConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonCacheManagerUtils {

  private final RedissonUtils redissonUtils;

  /**
   * Get Cache Manager
   *
   * @param host Redis Host
   * @return Cache Manager
   */
  public CacheManager getCacheManager(String host) {
    return getCacheManager(host, null, null);
  }

  /**
   * Get Cache Manager
   *
   * @param host Redis Host
   * @param ttlConfig Cache TTL Config
   * @return Cache Manager
   */
  public CacheManager getCacheManager(String host, CacheTTLConfig ttlConfig) {
    return getCacheManager(host, ttlConfig, null);
  }

  /**
   * Get Cache Manager
   *
   * @param host Redis Host
   * @param configMap Cache TTL Config Map
   * @return Cache Manager
   */
  public CacheManager getCacheManager(String host, Map<String, CacheTTLConfig> configMap) {
    return getCacheManager(host, null, configMap);
  }

  /**
   * Get Cache Manager
   *
   * @param host Redis Host
   * @param ttlConfig Cache TTL Config default
   * @param configMap Cache TTL Config Map
   * @return Cache Manager
   */
  public CacheManager getCacheManager(String host, CacheTTLConfig ttlConfig, Map<String, CacheTTLConfig> configMap) {
    if (host == null || host.isEmpty()) {
      return new NoOpCacheManager();
    }
    RedissonClient redissonClient = redissonUtils.getRedissonClient(host);
    return new RedissonCacheManager(redissonClient, ttlConfig, configMap);
  }

  /**
   * Get Compressed Cache Manager
   *
   * @param host Redis Host
   * @return Cache Manager
   */
  public CacheManager getCompressedCacheManager(String host) {
    return getCompressedCacheManager(host, null, null);
  }

  /**
   * Get Compressed Cache Manager
   *
   * @param host Redis Host
   * @param ttlConfig Cache TTL Config
   * @return Cache Manager
   */
  public CacheManager getCompressedCacheManager(String host, CacheTTLConfig ttlConfig) {
    return getCompressedCacheManager(host, ttlConfig, null);
  }

  /**
   * Get Compressed Cache Manager
   *
   * @param host Redis Host
   * @param configMap Cache TTL Config Map
   * @return Cache Manager
   */
  public CacheManager getCompressedCacheManager(String host, Map<String, CacheTTLConfig> configMap) {
    return getCompressedCacheManager(host, null, configMap);
  }

  /**
   * Get Compressed Cache Manager
   *
   * @param host Redis Host
   * @param ttlConfig Cache TTL Config default
   * @param configMap Cache TTL Config Map
   * @return Cache Manager
   */
  public CacheManager getCompressedCacheManager(String host, CacheTTLConfig ttlConfig, Map<String, CacheTTLConfig> configMap) {
    if (host == null || host.isEmpty()) {
      return new NoOpCacheManager();
    }
    RedissonClient redissonClient = redissonUtils.getCompressedRedissonClient(host);
    return new RedissonCacheManager(redissonClient, ttlConfig, configMap);
  }
}
