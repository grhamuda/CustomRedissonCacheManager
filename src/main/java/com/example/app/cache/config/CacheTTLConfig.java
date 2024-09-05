package com.example.app.cache.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

@Value
@Builder
@AllArgsConstructor
public class CacheTTLConfig implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  Long ttlInMinutes;
  boolean randomizeTTL;
  Long minRandomTTLInMinutes;
  Long maxRandomTTLInMinutes;

  /**
   * Construct CacheTTLConfig with TTL in minutes with Duration input
   *
   * @param ttl TTL in Duration, will convert to minutes
   * @return CacheTTLConfig
   */
  public static CacheTTLConfig constructTTLConfig(Duration ttl) {
    return CacheTTLConfig.builder()
        .ttlInMinutes(ttl.toMinutes())
        .build();
  }

  /**
   * Construct CacheTTLConfig with randomized TTL in minutes with Duration input
   *
   * @param baseTTL Base TTL in Duration, will convert to minutes
   * @param minRandomTTL Minimum Random TTL in Duration, will convert to minutes
   * @param maxRandomTTL Maximum Random TTL in Duration, will convert to minutes
   * @return CacheTTLConfig
   */
  public static CacheTTLConfig constructRandomizedTTLConfig(Duration baseTTL, Duration minRandomTTL, Duration maxRandomTTL) {
    return CacheTTLConfig.builder()
        .ttlInMinutes(baseTTL.toMinutes())
        .randomizeTTL(true)
        .minRandomTTLInMinutes(minRandomTTL.toMinutes())
        .maxRandomTTLInMinutes(maxRandomTTL.toMinutes())
        .build();
  }

  /**
   * Construct CacheTTLConfig with TTL in minutes
   *
   * @param ttl TTL in minutes
   * @return CacheTTLConfig
   */
  public static CacheTTLConfig constructTTLConfigInMinutes(long ttl) {
    return CacheTTLConfig.builder()
        .ttlInMinutes(ttl)
        .build();
  }

  /**
   * Construct CacheTTLConfig with randomized TTL in minutes
   *
   * @param baseTTL Base TTL in minutes
   * @param minRandomTTL Minimum Random TTL in minutes
   * @param maxRandomTTL Maximum Random TTL in minutes
   * @return CacheTTLConfig
   */
  public static CacheTTLConfig constructRandomizedTTLConfigInMinutes(long baseTTL, long minRandomTTL, long maxRandomTTL) {
    return CacheTTLConfig.builder()
        .ttlInMinutes(baseTTL)
        .randomizeTTL(true)
        .minRandomTTLInMinutes(minRandomTTL)
        .maxRandomTTLInMinutes(maxRandomTTL)
        .build();
  }
}
