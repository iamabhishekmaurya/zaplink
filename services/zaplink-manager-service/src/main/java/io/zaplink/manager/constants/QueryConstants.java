package io.zaplink.manager.constants;

public final class QueryConstants
{
    // UrlMappingEntity Queries
    public static final String FIND_EXPIRED_URLS                            = "SELECT u FROM url_mapping u WHERE u.expiresAt IS NOT NULL AND u.expiresAt < :currentTime";
    public static final String INCREMENT_CLICK_COUNT                        = "UPDATE url_mapping u SET u.clickCount = u.clickCount + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String UPDATE_STATUS                                = "UPDATE url_mapping u SET u.status = :status WHERE u.shortUrlKey = :shortUrlKey";
    public static final String FIND_BY_CREATED_AT_BETWEEN                   = "SELECT u FROM url_mapping u WHERE u.createdAt BETWEEN :startDate AND :endDate";
    public static final String FIND_MOST_CLICKED_URLS                       = "SELECT u FROM url_mapping u ORDER BY u.clickCount DESC";
    public static final String FIND_ACTIVE_URLS                             = "SELECT u FROM url_mapping u WHERE u.status = 'ACTIVE' AND (u.expiresAt IS NULL OR u.expiresAt > :currentTime)";
    public static final String FIND_BY_ORIGINAL_URL_CONTAINING              = "SELECT u FROM url_mapping u WHERE u.originalUrl LIKE %:urlPattern%";
    // UrlAnalyticsEntity Queries
    public static final String COUNT_CLICKS_BY_SHORT_URL_KEY                = "SELECT COUNT(u) FROM url_analytics u WHERE u.shortUrlKey = :shortUrlKey";
    public static final String COUNT_CLICKS_BY_SHORT_URL_KEY_AND_DATE_RANGE = "SELECT COUNT(u) FROM url_analytics u WHERE u.shortUrlKey = :shortUrlKey AND u.accessedAt BETWEEN :startDate AND :endDate";
    public static final String FIND_RECENT_ANALYTICS_BY_SHORT_URL_KEY       = "SELECT u FROM url_analytics u WHERE u.shortUrlKey = :shortUrlKey ORDER BY u.accessedAt DESC";
    public static final String FIND_TOP_COUNTRIES_BY_CLICKS                 = "SELECT u.country, COUNT(u) as clickCount FROM url_analytics u WHERE u.country IS NOT NULL GROUP BY u.country ORDER BY clickCount DESC";
    public static final String FIND_TOP_CITIES_BY_CLICKS                    = "SELECT u.city, COUNT(u) as clickCount FROM url_analytics u WHERE u.city IS NOT NULL GROUP BY u.city ORDER BY clickCount DESC";
    public static final String FIND_BY_ACCESSED_AT_BETWEEN                  = "SELECT u FROM url_analytics u WHERE u.accessedAt BETWEEN :startDate AND :endDate";
    public static final String COUNT_UNIQUE_IPS_BY_SHORT_URL_KEY            = "SELECT COUNT(DISTINCT u.ipAddress) FROM url_analytics u WHERE u.shortUrlKey = :shortUrlKey AND u.ipAddress IS NOT NULL";
    public static final String FIND_DAILY_CLICK_COUNTS                      = "SELECT DATE(u.accessedAt), COUNT(u) FROM url_analytics u WHERE u.shortUrlKey = :shortUrlKey GROUP BY DATE(u.accessedAt) ORDER BY DATE(u.accessedAt)";
    public static final String FIND_HOURLY_CLICK_COUNTS                     = "SELECT EXTRACT(HOUR FROM u.accessedAt), COUNT(u) FROM url_analytics u WHERE u.shortUrlKey = :shortUrlKey GROUP BY EXTRACT(HOUR FROM u.accessedAt) ORDER BY EXTRACT(HOUR FROM u.accessedAt)";
    public static final String FIND_TOP_REFERRERS_BY_CLICKS                 = "SELECT u.referrer, COUNT(u) as clickCount FROM url_analytics u WHERE u.referrer IS NOT NULL GROUP BY u.referrer ORDER BY clickCount DESC";
    public static final String FIND_TOP_USER_AGENTS_BY_CLICKS               = "SELECT u.userAgent, COUNT(u) as clickCount FROM url_analytics u WHERE u.userAgent IS NOT NULL GROUP BY u.userAgent ORDER BY clickCount DESC";
    // UrlStatisticsEntity Queries
    public static final String INCREMENT_TOTAL_CLICKS                       = "UPDATE url_statistics u SET u.totalClicks = u.totalClicks + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String INCREMENT_TODAY_CLICKS                       = "UPDATE url_statistics u SET u.todayClicks = u.todayClicks + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String UPDATE_LAST_ACCESSED                         = "UPDATE url_statistics u SET u.lastAccessed = :lastAccessed WHERE u.shortUrlKey = :shortUrlKey";
    public static final String RESET_TODAY_CLICKS                           = "UPDATE url_statistics u SET u.todayClicks = 0";
    public static final String FIND_MOST_CLICKED_URLS_STATS                 = "SELECT u FROM url_statistics u ORDER BY u.totalClicks DESC";
    public static final String FIND_MOST_TODAY_CLICKED_URLS                 = "SELECT u FROM url_statistics u ORDER BY u.todayClicks DESC";
    public static final String FIND_RECENTLY_ACCESSED_URLS                  = "SELECT u FROM url_statistics u WHERE u.lastAccessed IS NOT NULL ORDER BY u.lastAccessed DESC";
    public static final String FIND_BY_LAST_ACCESSED_BETWEEN                = "SELECT u FROM url_statistics u WHERE u.lastAccessed BETWEEN :startDate AND :endDate";
    public static final String COUNT_URLS_WITH_TODAY_CLICKS                 = "SELECT COUNT(u) FROM url_statistics u WHERE u.todayClicks > 0";
    public static final String GET_TOTAL_CLICKS_ACROSS_ALL_URLS             = "SELECT SUM(u.totalClicks) FROM url_statistics u";
    public static final String GET_TODAY_CLICKS_ACROSS_ALL_URLS             = "SELECT SUM(u.todayClicks) FROM url_statistics u";
    public static final String FIND_BY_MIN_TOTAL_CLICKS                     = "SELECT u FROM url_statistics u WHERE u.totalClicks >= :minClicks";
    public static final String FIND_BY_MIN_TODAY_CLICKS                     = "SELECT u FROM url_statistics u WHERE u.todayClicks >= :minTodayClicks";
    public static final String GET_CLICK_STATISTICS_SUMMARY                 = "SELECT COUNT(u), SUM(u.totalClicks), AVG(u.totalClicks), MAX(u.totalClicks) FROM url_statistics u";
    public static final String FIND_INACTIVE_URLS                           = "SELECT u FROM url_statistics u WHERE u.lastAccessed < :cutoffDate OR u.lastAccessed IS NULL";
    private QueryConstants()
    {
        // Utility class - prevent instantiation
    }
}
