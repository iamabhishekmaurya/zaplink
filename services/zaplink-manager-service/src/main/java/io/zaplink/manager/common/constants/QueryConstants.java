package io.zaplink.manager.common.constants;

public final class QueryConstants
{
    // UrlMappingEntity Queries
    public static final String FIND_EXPIRED_URLS                            = "SELECT u FROM UrlMappingEntity u WHERE u.expiresAt IS NOT NULL AND u.expiresAt < :currentTime";
    public static final String INCREMENT_CLICK_COUNT                        = "UPDATE UrlMappingEntity u SET u.clickCount = u.clickCount + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String UPDATE_STATUS                                = "UPDATE UrlMappingEntity u SET u.status = :status WHERE u.shortUrlKey = :shortUrlKey";
    public static final String FIND_BY_CREATED_AT_BETWEEN                   = "SELECT u FROM UrlMappingEntity u WHERE u.createdAt BETWEEN :startDate AND :endDate";
    public static final String FIND_MOST_CLICKED_URLS                       = "SELECT u FROM UrlMappingEntity u ORDER BY u.clickCount DESC";
    public static final String FIND_ACTIVE_URLS                             = "SELECT u FROM UrlMappingEntity u WHERE u.status = 'ACTIVE' AND (u.expiresAt IS NULL OR u.expiresAt > :currentTime)";
    public static final String FIND_BY_ORIGINAL_URL_CONTAINING              = "SELECT u FROM UrlMappingEntity u WHERE u.originalUrl LIKE %:urlPattern%";
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
    public static final String FIND_TOP_REFERRERS_BY_CLICKS                 = "SELECT u.referrer, COUNT(u) as clickCount FROM UrlAnalyticsEntity u WHERE u.referrer IS NOT NULL GROUP BY u.referrer ORDER BY clickCount DESC";
    public static final String FIND_TOP_USER_AGENTS_BY_CLICKS               = "SELECT u.userAgent, COUNT(u) as clickCount FROM UrlAnalyticsEntity u WHERE u.userAgent IS NOT NULL GROUP BY u.userAgent ORDER BY clickCount DESC";
    // User Stats Queries
    public static final String FIND_CLICK_TREND_BY_USER                     = "SELECT DATE(a.accessedAt), COUNT(a) FROM UrlAnalyticsEntity a, UrlMappingEntity m WHERE a.shortUrlKey = m.shortUrlKey AND m.userEmail = :userEmail GROUP BY DATE(a.accessedAt) ORDER BY DATE(a.accessedAt)";
    public static final String FIND_TOP_REFERRERS_BY_USER                   = "SELECT a.referrer, COUNT(a) as clickCount FROM UrlAnalyticsEntity a, UrlMappingEntity m WHERE a.shortUrlKey = m.shortUrlKey AND m.userEmail = :userEmail AND a.referrer IS NOT NULL GROUP BY a.referrer ORDER BY clickCount DESC";
    public static final String FIND_TOP_REGIONS_BY_USER                     = "SELECT a.country, COUNT(a) as clickCount FROM UrlAnalyticsEntity a, UrlMappingEntity m WHERE a.shortUrlKey = m.shortUrlKey AND m.userEmail = :userEmail AND a.country IS NOT NULL GROUP BY a.country ORDER BY clickCount DESC";
    // UrlStatisticsEntity Queries
    public static final String INCREMENT_TOTAL_CLICKS                       = "UPDATE UrlStatisticsEntity u SET u.totalClicks = u.totalClicks + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String INCREMENT_TODAY_CLICKS                       = "UPDATE UrlStatisticsEntity u SET u.todayClicks = u.todayClicks + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String UPDATE_LAST_ACCESSED                         = "UPDATE UrlStatisticsEntity u SET u.lastAccessed = :lastAccessed WHERE u.shortUrlKey = :shortUrlKey";
    public static final String RESET_TODAY_CLICKS                           = "UPDATE UrlStatisticsEntity u SET u.todayClicks = 0";
    public static final String FIND_MOST_CLICKED_URLS_STATS                 = "SELECT u FROM UrlStatisticsEntity u ORDER BY u.totalClicks DESC";
    public static final String FIND_MOST_TODAY_CLICKED_URLS                 = "SELECT u FROM UrlStatisticsEntity u ORDER BY u.todayClicks DESC";
    public static final String FIND_RECENTLY_ACCESSED_URLS                  = "SELECT u FROM UrlStatisticsEntity u WHERE u.lastAccessed IS NOT NULL ORDER BY u.lastAccessed DESC";
    public static final String FIND_BY_LAST_ACCESSED_BETWEEN                = "SELECT u FROM UrlStatisticsEntity u WHERE u.lastAccessed BETWEEN :startDate AND :endDate";
    public static final String COUNT_URLS_WITH_TODAY_CLICKS                 = "SELECT COUNT(u) FROM UrlStatisticsEntity u WHERE u.todayClicks > 0";
    public static final String GET_TOTAL_CLICKS_ACROSS_ALL_URLS             = "SELECT SUM(u.totalClicks) FROM UrlStatisticsEntity u";
    public static final String GET_TODAY_CLICKS_ACROSS_ALL_URLS             = "SELECT SUM(u.todayClicks) FROM UrlStatisticsEntity u";
    public static final String FIND_BY_MIN_TOTAL_CLICKS                     = "SELECT u FROM UrlStatisticsEntity u WHERE u.totalClicks >= :minClicks";
    public static final String FIND_BY_MIN_TODAY_CLICKS                     = "SELECT u FROM UrlStatisticsEntity u WHERE u.todayClicks >= :minTodayClicks";
    public static final String GET_CLICK_STATISTICS_SUMMARY                 = "SELECT COUNT(u), SUM(u.totalClicks), AVG(u.totalClicks), MAX(u.totalClicks) FROM UrlStatisticsEntity u";
    public static final String FIND_INACTIVE_URLS                           = "SELECT u FROM UrlStatisticsEntity u WHERE u.lastAccessed < :cutoffDate OR u.lastAccessed IS NULL";
    private QueryConstants()
    {
        // Utility class - prevent instantiation
    }
}
