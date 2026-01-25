package io.zaplink.redirect.common.constants;

import java.time.Duration;

/**
 * Redis caching constants.
 */
public final class RedisConstants
{
    private RedisConstants()
    {
        // Utility class
    }
    public static final String   URL_CACHE_PREFIX = "url:";
    public static final String   QR_CACHE_PREFIX  = "qr:";
    public static final Duration URL_CACHE_TTL    = Duration.ofHours( 24 );
    public static final Duration QR_CACHE_TTL     = Duration.ofHours( 12 );
}
