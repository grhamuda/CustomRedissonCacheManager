package com.example.app.cache.codec;

import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.codec.SerializationCodec;
import org.redisson.codec.SnappyCodecV2;

public class RedissonCacheCompressionCodec extends BaseCodec {
  // use snappy codec v2 for value encoding and decoding
  private final SnappyCodecV2 snappyCodecV2 = new SnappyCodecV2();

  // user serialization codec for map key encoding and decoding
  private final SerializationCodec serializationCodec = new SerializationCodec();

  @Override
  public ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  @Override
  public Decoder<Object> getValueDecoder() {
    return snappyCodecV2.getValueDecoder();
  }

  @Override
  public Encoder getValueEncoder() {
    return snappyCodecV2.getValueEncoder();
  }

  @Override
  public Decoder<Object> getMapValueDecoder() {
    return snappyCodecV2.getMapValueDecoder();
  }

  @Override
  public Decoder<Object> getMapKeyDecoder() {
    return serializationCodec.getMapKeyDecoder();
  }

  @Override
  public Encoder getMapKeyEncoder() {
    return serializationCodec.getMapKeyEncoder();
  }

  @Override
  public Encoder getMapValueEncoder() {
    return snappyCodecV2.getMapValueEncoder();
  }
}
