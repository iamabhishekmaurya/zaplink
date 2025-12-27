package io.zaplink.manager.common.constants;

/**
 * Redis-related constants.
 * 
 * @author Zaplink Team
 */
public final class RedisConstants
{
    private RedisConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    public static final long URL_CACHE_TTL = 60 * 60 * 24; // 24 Hours
}
