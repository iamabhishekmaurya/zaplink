package io.zaplink.manager.service.helper;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.manager.common.constants.LogConstants;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class RedisServiceHelper
{
    private RedisTemplate<String, Object> redisTemplate;
    public RedisServiceHelper( RedisTemplate<String, Object> redisTemplate )
    {
        this.redisTemplate = redisTemplate;
    }

    // Set key-value with TTL
    public void setValue( String key, String value, long timeoutInSeconds )
    {
        log.info( LogConstants.LOG_REDIS_SET_KEY, key, value );
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
