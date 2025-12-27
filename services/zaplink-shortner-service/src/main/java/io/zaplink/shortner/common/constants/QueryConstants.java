package io.zaplink.shortner.common.constants;

public final class QueryConstants
{
    // UrlMappingEntity Queries
    public static final String FIND_EXPIRED_URLS          = "SELECT u FROM UrlMappingEntity u WHERE u.expiresAt IS NOT NULL AND u.expiresAt < :currentTime";
    public static final String INCREMENT_CLICK_COUNT      = "UPDATE UrlMappingEntity u SET u.clickCount = u.clickCount + 1 WHERE u.shortUrlKey = :shortUrlKey";
    public static final String UPDATE_STATUS              = "UPDATE UrlMappingEntity u SET u.status = :status WHERE u.shortUrlKey = :shortUrlKey";
    public static final String FIND_BY_CREATED_AT_BETWEEN = "SELECT u FROM UrlMappingEntity u WHERE u.createdAt BETWEEN :startDate AND :endDate";
    public static final String FIND_MOST_CLICKED_URLS     = "SELECT u FROM UrlMappingEntity u ORDER BY u.clickCount DESC";
    private QueryConstants()
    {
        // Utility class - prevent instantiation
    }
}
