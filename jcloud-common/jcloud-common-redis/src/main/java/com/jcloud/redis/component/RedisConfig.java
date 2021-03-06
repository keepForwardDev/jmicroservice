package com.jcloud.redis.component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Primary
    @SuppressWarnings(value = {"unchecked", "rawtypes", "deprecation"})
    public RedisTemplate<String, Object> redisTemplate() {
        return getRedisTemplate(redisConnectionFactory);
    }

    public static RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // ????????????????????????java??????
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // ??????StringResDataedisSerializer???????????????????????????redis???key???
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * ????????????, ???????????????????????????
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        // ??????null ??????
        RedisCacheConfiguration redisCacheConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30)); // ?????????????????????30?????????
        configMap.put("redisCacheManager", redisCacheConfig);
        RedisCacheWriter cacheWriter =
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheManager redisCacheManager =
                new RedisCacheManager(
                        cacheWriter,
                        RedisCacheConfiguration.defaultCacheConfig(), // ?????????????????????
                        configMap);
        return redisCacheManager;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    public class CustomCacheErrorHandler extends SimpleCacheErrorHandler {
        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
            System.out.println(key);
        }
    }


}
