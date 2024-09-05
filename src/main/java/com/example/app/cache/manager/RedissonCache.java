package com.example.app.cache.manager;

import com.example.app.cache.config.CacheTTLConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class RedissonCache implements Cache {

  private final String name;
  private final RedissonClient redissonClient;
  private final CacheTTLConfig cacheTTLConfig;

  private static final SecureRandom random = new SecureRandom();

  @Override
  public @NonNull String getName() {
    log.debug("[debug] [cache] get cache name: {}", this.name);
    return this.name;
  }

  @Override
  public @NonNull Object getNativeCache() {
    return this.redissonClient;
  }

  @Override
  public ValueWrapper get(@NonNull Object key) {
    String cacheKey = constructKey(key);
    log.info("[md-debug] [cache] get cache key: {}", cacheKey);
    RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
    Object value = bucket.get();
    return (value != null ? new SimpleValueWrapper(value) : null);
  }

  @Override
  public <T> T get(@NonNull Object key, Class<T> type) {
    String cacheKey = constructKey(key);
    log.debug("[debug] [cache] get cache with class type : {}", cacheKey);
    RBucket<T> bucket = redissonClient.getBucket(cacheKey);
    return bucket.get();
  }

  @Override
  public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
    String cacheKey = constructKey(key);
    log.info("[debug] [cache] get cache with value loader : {}", cacheKey);
    RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
    Object value = bucket.get();
    if (value != null) {
      return (T) value;
    } else {
      try {
        T newValue = valueLoader.call();
        put(key, newValue);
        return newValue;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void put(@NonNull Object key, Object value) {
    String cacheKey = constructKey(key);
    log.debug("[debug] [cache] put cache key : {}", cacheKey);
    RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
    Duration ttl = getCacheTTL();
    if (ttl != null) {
      bucket.set(value, ttl.toMinutes(), TimeUnit.MINUTES);
    } else {
      bucket.set(value);
    }
  }

  @Override
  public ValueWrapper putIfAbsent(@NonNull Object key, Object value) {
    String cacheKey = constructKey(key);
    log.debug("[debug] [cache] put if absent : {}", cacheKey);
    RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
    boolean isSet;
    Duration ttl = getCacheTTL();
    if (ttl != null) {
      isSet = bucket.setIfAbsent(value, ttl);
    } else {
      isSet = bucket.setIfAbsent(value);
    }
    return (isSet ? new SimpleValueWrapper(value) : null);
  }

  @Override
  public void evict(@NonNull Object key) {
    String cacheKey = constructKey(key);
    log.debug("[debug] [cache] evict cache key : {}", cacheKey);
    RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
    bucket.delete();
  }

  @Override
  public void clear() {
    redissonClient.getKeys().deleteByPattern(name + "*");
  }

  private String constructKey(Object key) {
    if (key instanceof List<?> keys) {
      // Use StringJoiner to join list elements with a comma
      StringJoiner joiner = new StringJoiner(",");
      for (Object element : keys) {
        joiner.add(element.toString());
      }
      return name + "::" + joiner;
    }
    return name + "::" + key.toString();
  }

  /**
   * Get cache TTL
   *
   * @return cache TTL, duration in minutes
   */
  private Duration getCacheTTL() {
    if (cacheTTLConfig == null || cacheTTLConfig.getTtlInMinutes() == null || cacheTTLConfig.getTtlInMinutes() <= 0) {
      return null;
    }

    if (cacheTTLConfig.isRandomizeTTL()) {
      return randomizeTTL(cacheTTLConfig.getTtlInMinutes(), cacheTTLConfig.getTtlInMinutes(), cacheTTLConfig.getMaxRandomTTLInMinutes());
    }

    return Duration.ofMinutes(cacheTTLConfig.getTtlInMinutes());
  }

  /**
   * Calculate randomized cache TTL
   *
   * @param baseTTL base cache TTL
   * @param min minimum random TTL
   * @param max maximum random TTL
   * @return randomized cache TTL, duration in minutes
   */
  private Duration randomizeTTL(Long baseTTL, Long min, Long max) {
    long newTTL = baseTTL + random.nextLong(min, max);
    return Duration.ofMinutes(newTTL);
  }
}
