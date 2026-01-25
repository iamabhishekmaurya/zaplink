package io.zaplink.redirect.service;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.redirect.common.constants.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis service for URL and QR code caching.
 * Provides fast lookups for redirect resolution.
 */
@Service @Slf4j @RequiredArgsConstructor
public class RedisService
{
    private final StringRedisTemplate redisTemplate;
    /**
     * Get cached URL by short URL key.
     *
     * @param shortUrlKey the short URL key
     * @return Optional containing the original URL if cached
     */
    public Optional<String> getUrl( String shortUrlKey )
    {
        try
        {
            String key = RedisConstants.URL_CACHE_PREFIX + shortUrlKey;
            String value = redisTemplate.opsForValue().get( key );
            if ( value != null )
            {
                log.debug( "🔵 Cache HIT for URL key: {}", shortUrlKey );
                return Optional.of( value );
            }
            log.debug( "🔴 Cache MISS for URL key: {}", shortUrlKey );
            return Optional.empty();
        }
        catch ( Exception e )
        {
            log.warn( "Redis error fetching URL key: {}", shortUrlKey, e );
            return Optional.empty();
        }
    }

    /**
     * Cache URL for fast lookups.
     *
     * @param shortUrlKey the short URL key
     * @param originalUrl the original URL to cache
     */
    public void cacheUrl( String shortUrlKey, String originalUrl )
    {
        try
        {
            String key = RedisConstants.URL_CACHE_PREFIX + shortUrlKey;
            redisTemplate.opsForValue().set( key, originalUrl, RedisConstants.URL_CACHE_TTL );
            log.debug( "📝 Cached URL for key: {}", shortUrlKey );
        }
        catch ( Exception e )
        {
            log.warn( "Redis error caching URL key: {}", shortUrlKey, e );
        }
    }

    /**
     * Get cached QR destination URL by QR key.
     *
     * @param qrKey the QR key
     * @return Optional containing the destination URL if cached
     */
    public Optional<String> getQrDestination( String qrKey )
    {
        try
        {
            String key = RedisConstants.QR_CACHE_PREFIX + qrKey;
            String value = redisTemplate.opsForValue().get( key );
            if ( value != null )
            {
                log.debug( "🔵 Cache HIT for QR key: {}", qrKey );
                return Optional.of( value );
            }
            log.debug( "🔴 Cache MISS for QR key: {}", qrKey );
            return Optional.empty();
        }
        catch ( Exception e )
        {
            log.warn( "Redis error fetching QR key: {}", qrKey, e );
            return Optional.empty();
        }
    }

    /**
     * Cache QR destination URL for fast lookups.
     * Note: QR codes with advanced features (password, limits, etc.) should NOT be cached.
     *
     * @param qrKey          the QR key
     * @param destinationUrl the destination URL to cache
     */
    public void cacheQrDestination( String qrKey, String destinationUrl )
    {
        try
        {
            String key = RedisConstants.QR_CACHE_PREFIX + qrKey;
            redisTemplate.opsForValue().set( key, destinationUrl, RedisConstants.QR_CACHE_TTL );
            log.debug( "📝 Cached QR destination for key: {}", qrKey );
        }
        catch ( Exception e )
        {
            log.warn( "Redis error caching QR key: {}", qrKey, e );
        }
    }

    /**
     * Invalidate cached QR destination.
     *
     * @param qrKey the QR key to invalidate
     */
    public void invalidateQrCache( String qrKey )
    {
        try
        {
            String key = RedisConstants.QR_CACHE_PREFIX + qrKey;
            redisTemplate.delete( key );
            log.debug( "🗑️ Invalidated cache for QR key: {}", qrKey );
        }
        catch ( Exception e )
        {
            log.warn( "Redis error invalidating QR key: {}", qrKey, e );
        }
    }
}
