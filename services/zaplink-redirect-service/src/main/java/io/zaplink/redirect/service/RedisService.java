package io.zaplink.redirect.service;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.redirect.common.constants.RedisConstants;
import io.zaplink.redirect.dto.RedirectConfigDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Redis service for URL and QR code caching.
 * Provides fast lookups for redirect resolution.
 */
@Service @Slf4j @RequiredArgsConstructor
public class RedisService
{
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper        objectMapper;
    /**
     * Get cached URL config (original URL + rules) by short URL key.
     */
    public Optional<RedirectConfigDto> getUrlConfig( String shortUrlKey )
    {
        try
        {
            String key = RedisConstants.URL_CACHE_PREFIX + "config:" + shortUrlKey;
            String value = redisTemplate.opsForValue().get( key );
            if ( value != null )
            {
                log.debug( "🔵 Cache HIT for URL config: {}", shortUrlKey );
                return Optional.of( objectMapper.readValue( value, RedirectConfigDto.class ) );
            }
            log.debug( "🔴 Cache MISS for URL config: {}", shortUrlKey );
            return Optional.empty();
        }
        catch ( Exception e )
        {
            log.warn( "Redis error fetching URL config: {}", shortUrlKey, e );
            return Optional.empty();
        }
    }

    /**
     * Cache URL Configuration.
     */
    public void cacheUrlConfig( String shortUrlKey, RedirectConfigDto config )
    {
        try
        {
            String key = RedisConstants.URL_CACHE_PREFIX + "config:" + shortUrlKey;
            String json = objectMapper.writeValueAsString( config );
            redisTemplate.opsForValue().set( key, json, RedisConstants.URL_CACHE_TTL );
            log.debug( "📝 Cached URL config for key: {}", shortUrlKey );
        }
        catch ( Exception e )
        {
            log.warn( "Redis error caching URL config: {}", shortUrlKey, e );
        }
    }

    /**
     * Get cached QR config by QR key.
     */
    public Optional<RedirectConfigDto> getQrConfig( String qrKey )
    {
        try
        {
            String key = RedisConstants.QR_CACHE_PREFIX + "config:" + qrKey;
            String value = redisTemplate.opsForValue().get( key );
            if ( value != null )
            {
                log.debug( "🔵 Cache HIT for QR config: {}", qrKey );
                return Optional.of( objectMapper.readValue( value, RedirectConfigDto.class ) );
            }
            log.debug( "🔴 Cache MISS for QR config: {}", qrKey );
            return Optional.empty();
        }
        catch ( Exception e )
        {
            log.warn( "Redis error fetching QR config: {}", qrKey, e );
            return Optional.empty();
        }
    }

    /**
     * Cache QR Config.
     */
    public void cacheQrConfig( String qrKey, RedirectConfigDto config )
    {
        try
        {
            String key = RedisConstants.QR_CACHE_PREFIX + "config:" + qrKey;
            String json = objectMapper.writeValueAsString( config );
            redisTemplate.opsForValue().set( key, json, RedisConstants.QR_CACHE_TTL );
            log.debug( "📝 Cached QR config for key: {}", qrKey );
        }
        catch ( Exception e )
        {
            log.warn( "Redis error caching QR config: {}", qrKey, e );
        }
    }

    /**
     * Deprecated: Get cached URL by short URL key.
     * Kept for backward compatibility if needed, but new logic should use getUrlConfig.
     */
    public Optional<String> getUrl( String shortUrlKey )
    {
        // Fallback to simple string cache
        return Optional.empty();
    }

    /**
     * Deprecated: Cache URL for fast lookups.
     */
    public void cacheUrl( String shortUrlKey, String originalUrl )
    {
        // No-op or delegate to old logic if needed
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
