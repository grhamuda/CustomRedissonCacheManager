package com.example.app.cache.manager;

import com.example.app.cache.config.CacheTTLConfig;
import lombok.NonNull;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedissonCacheManager implements CacheManager {

  private final RedissonClient redissonClient;
  private final ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
  private final Map<String, CacheTTLConfig> cacheTTLConfigMap = new ConcurrentHashMap<>();
  private final CacheTTLConfig defaultCacheTTLConfig;

  /**
   * Constructor
   *
   * @param redissonClient Redisson Client
   */
  public RedissonCacheManager(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
    this.defaultCacheTTLConfig = null;
  }

  /**
   * Constructor
   *
   * @param redissonClient Redisson Client
   * @param cacheTTLConfigMap Cache TTL Config Map
   */
  public RedissonCacheManager(RedissonClient redissonClient, Map<String, CacheTTLConfig> cacheTTLConfigMap) {
    this.redissonClient = redissonClient;
    this.defaultCacheTTLConfig = null;
    this.cacheTTLConfigMap.putAll(cacheTTLConfigMap);
  }

  /**
   * Constructor
   *
   * @param redissonClient Redisson Client
   * @param defaultCacheTTLConfig Default Cache TTL Config, this config will be used if cache name is not found in cacheTTLConfigMap
   * @param cacheTTLConfigMap Cache TTL Config Map
   */
  public RedissonCacheManager(RedissonClient redissonClient, CacheTTLConfig defaultCacheTTLConfig, Map<String, CacheTTLConfig> cacheTTLConfigMap) {
    this.redissonClient = redissonClient;
    this.defaultCacheTTLConfig = defaultCacheTTLConfig;

    if (cacheTTLConfigMap != null && !cacheTTLConfigMap.isEmpty()) {
      this.cacheTTLConfigMap.putAll(cacheTTLConfigMap);
    }
  }

  @Override
  public Cache getCache(@NonNull String name) {
    return cacheMap.computeIfAbsent(name, k -> new RedissonCache(k, redissonClient, getCacheTTLConfig(name)));
  }

  @Override
  public @NonNull Collection<String> getCacheNames() {
    return cacheMap.keySet();
  }

  private CacheTTLConfig getCacheTTLConfig(String cacheName) {
    return cacheTTLConfigMap.getOrDefault(cacheName, defaultCacheTTLConfig);
  }
}
