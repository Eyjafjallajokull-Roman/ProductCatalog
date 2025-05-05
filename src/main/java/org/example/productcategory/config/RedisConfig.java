package org.example.productcategory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);

    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
    template.setValueSerializer(serializer);
    template.setHashValueSerializer(serializer);

    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());

    template.afterPropertiesSet();
    return template;
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            )
            .entryTtl(Duration.ofMinutes(30));

    Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
    cacheConfigs.put("fixerRates", config);

    return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .withInitialCacheConfigurations(cacheConfigs)
            .build();
  }
}