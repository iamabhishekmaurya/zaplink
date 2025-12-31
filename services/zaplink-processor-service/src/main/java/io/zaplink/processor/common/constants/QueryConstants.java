package io.zaplink.processor.common.constants;

public final class QueryConstants
{
    // UrlMappingEntity Queries
    public static final String FIND_EXPIRED_URLS                            = "SELECT u FROM UrlMappingEntity u WHERE u.expiresAt IS NOT NULL AND u.expiresAt < :currentTime";
    public static final String INCREMENT_CLICK_COUNT                        = "UPDATE UrlMappingEntity u SET u.clickCount = u.clickCount + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String INCREMENT_CLICK_COUNT_BY                     = "UPDATE UrlMappingEntity u SET u.clickCount = u.clickCount + :incrementBy WHERE u.shortUrlKey = :shortUrlKey";
    public static final String UPDATE_STATUS                                = "UPDATE UrlMappingEntity u SET u.status = :status WHERE u.shortUrlKey = :shortUrlKey";
    public static final String FIND_BY_CREATED_AT_BETWEEN                   = "SELECT u FROM UrlMappingEntity u WHERE u.createdAt BETWEEN :startDate AND :endDate";
    public static final String FIND_MOST_CLICKED_URLS                       = "SELECT u FROM UrlMappingEntity u ORDER BY u.clickCount DESC";
    // UrlAnalyticsEntity Queries
    public static final String COUNT_CLICKS_BY_SHORT_URL_KEY                = "SELECT COUNT(u) FROM UrlAnalyticsEntity u WHERE u.shortUrlKey = :shortUrlKey";
    public static final String COUNT_CLICKS_BY_SHORT_URL_KEY_AND_DATE_RANGE = "SELECT COUNT(u) FROM UrlAnalyticsEntity u WHERE u.shortUrlKey = :shortUrlKey AND u.accessedAt BETWEEN :startDate AND :endDate";
    public static final String FIND_RECENT_ANALYTICS_BY_SHORT_URL_KEY       = "SELECT u FROM UrlAnalyticsEntity u WHERE u.shortUrlKey = :shortUrlKey ORDER BY u.accessedAt DESC";
    public static final String FIND_TOP_COUNTRIES_BY_CLICKS                 = "SELECT u.country, COUNT(u) as clickCount FROM UrlAnalyticsEntity u WHERE u.country IS NOT NULL GROUP BY u.country ORDER BY clickCount DESC";
    public static final String FIND_TOP_CITIES_BY_CLICKS                    = "SELECT u.city, COUNT(u) as clickCount FROM UrlAnalyticsEntity u WHERE u.city IS NOT NULL GROUP BY u.city ORDER BY clickCount DESC";
    public static final String FIND_BY_ACCESSED_AT_BETWEEN                  = "SELECT u FROM UrlAnalyticsEntity u WHERE u.accessedAt BETWEEN :startDate AND :endDate";
    public static final String COUNT_UNIQUE_IPS_BY_SHORT_URL_KEY            = "SELECT COUNT(DISTINCT u.ipAddress) FROM UrlAnalyticsEntity u WHERE u.shortUrlKey = :shortUrlKey AND u.ipAddress IS NOT NULL";
    public static final String FIND_DAILY_CLICK_COUNTS                      = "SELECT DATE(u.accessedAt), COUNT(u) FROM UrlAnalyticsEntity u WHERE u.shortUrlKey = :shortUrlKey GROUP BY DATE(u.accessedAt) ORDER BY DATE(u.accessedAt)";
    public static final String FIND_HOURLY_CLICK_COUNTS                     = "SELECT EXTRACT(HOUR FROM u.accessedAt), COUNT(u) FROM UrlAnalyticsEntity u WHERE u.shortUrlKey = :shortUrlKey GROUP BY EXTRACT(HOUR FROM u.accessedAt) ORDER BY EXTRACT(HOUR FROM u.accessedAt)";
    private QueryConstants()
    {
        // Utility class - prevent instantiation
    }
}
