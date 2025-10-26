package io.zaplink.zaplink_producer_service.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService
{
    private Logger                        logger = LoggerFactory.getLogger( RedisService.class );
    private RedisTemplate<String, Object> redisTemplate;
    public RedisService( RedisTemplate<String, Object> redisTemplate )
    {
        this.redisTemplate = redisTemplate;
    }

    // Set key-value with TTL
    public void setValue( String key, String value, long timeoutInSeconds )
    {
        logger.info( "Going to set kay: {} and value: {}", key, value );
        redisTemplate.opsForValue().set( key, value, timeoutInSeconds, TimeUnit.SECONDS );
    }

    // Get value by key
    public String getValue( String key )
    {
        Object value = redisTemplate.opsForValue().get( key );
        return value != null ? value.toString() : null;
    }

    // Delete a key
    public void deleteKey( String key )
    {
        redisTemplate.delete( key );
    }
}
