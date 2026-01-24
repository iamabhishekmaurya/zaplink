package io.zaplink.auth.common.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import io.zaplink.auth.common.constants.LogConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration for Redis Cache.
 * Sets up default TTL and serialization.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-24
 */
@Slf4j @Configuration
public class RedisConfig
{
    /**
     * Configures default Redis cache settings.
     * - Logic: Sets default TTL to 60 minutes.
     * - Serialization: Uses JSON for values (readable) instead of default JDK serialization.
     * 
     * @return RedisCacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration()
    {
        log.debug( LogConstants.LOG_CONFIGURING_REDIS_CACHE );
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl( Duration.ofMinutes( 60 ) ) // Default expiration: 60 mins
                .disableCachingNullValues().serializeValuesWith( RedisSerializationContext.SerializationPair
                        .fromSerializer( RedisSerializer.json() ) );
    }

    /**
     * Configures the RedisCacheManager.
     * Required for @EnableCaching to work.
     * 
     * @param redisConnectionFactory The connection factory
     * @return RedisCacheManager
     */
    @Bean
    public CacheManager cacheManager( RedisConnectionFactory redisConnectionFactory )
    {
        return RedisCacheManager.builder( redisConnectionFactory ).cacheDefaults( cacheConfiguration() ).build();
    }
}
