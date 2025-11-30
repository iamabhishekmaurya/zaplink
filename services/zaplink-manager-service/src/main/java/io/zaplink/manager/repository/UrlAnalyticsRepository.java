package io.zaplink.manager.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.constants.QueryConstants;
import io.zaplink.manager.entity.UrlAnalyticsEntity;

/**
 * Repository interface for URL analytics data persistence operations.
 * Provides methods to track and analyze URL access patterns, geographic distribution,
 * and user behavior metrics.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface UrlAnalyticsRepository
    extends
    JpaRepository<UrlAnalyticsEntity, BigInteger>
{
    /**
     * Retrieves all analytics records for a specific short URL key.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return List of analytics records for the specified URL key
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    List<UrlAnalyticsEntity> findByShortUrlKey( String shortUrlKey );

    /**
     * Finds all analytics records from a specific IP address.
     * Useful for tracking user activity and potential abuse patterns.
     * 
     * @param ipAddress the IP address to search for
     * @return List of analytics records from the specified IP address
     * @throws IllegalArgumentException if ipAddress is null or empty
     */
    List<UrlAnalyticsEntity> findByIpAddress( String ipAddress );

    /**
     * Retrieves analytics records filtered by country.
     * Used for geographic analysis of URL access patterns.
     * 
     * @param country the country name to filter by
     * @return List of analytics records from the specified country
     * @throws IllegalArgumentException if country is null or empty
     */
    List<UrlAnalyticsEntity> findByCountry( String country );

    /**
     * Finds analytics records for a specific city.
     * Provides granular geographic insights for URL access patterns.
     * 
     * @param city the city name to filter by
     * @return List of analytics records from the specified city
     * @throws IllegalArgumentException if city is null or empty
     */
    List<UrlAnalyticsEntity> findByCity( String city );

    /**
     * Retrieves analytics records by referrer source.
     * Helps analyze traffic sources and marketing effectiveness.
     * 
     * @param referrer the referrer URL or source to filter by
     * @return List of analytics records from the specified referrer
     * @throws IllegalArgumentException if referrer is null or empty
     */
    List<UrlAnalyticsEntity> findByReferrer( String referrer );

    /**
     * Counts total clicks for a specific short URL key across all time.
     * Provides basic popularity metrics for individual URLs.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return Total number of clicks for the specified URL key
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Query(QueryConstants.COUNT_CLICKS_BY_SHORT_URL_KEY)
    long countClicksByShortUrlKey( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Counts clicks for a specific URL within a defined date range.
     * Essential for time-based analysis and trend identification.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return Number of clicks within the specified date range
     * @throws IllegalArgumentException if any parameter is null or invalid
     */
    @Query(QueryConstants.COUNT_CLICKS_BY_SHORT_URL_KEY_AND_DATE_RANGE)
    long countClicksByShortUrlKeyAndDateRange( @Param("shortUrlKey") String shortUrlKey,
                                               @Param("startDate") Timestamp startDate,
                                               @Param("endDate") Timestamp endDate );

    /**
     * Retrieves the most recent analytics records for a specific URL.
     * Returns records ordered by access time in descending order.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return List of recent analytics records ordered by access time
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Query(QueryConstants.FIND_RECENT_ANALYTICS_BY_SHORT_URL_KEY)
    List<UrlAnalyticsEntity> findRecentAnalyticsByShortUrlKey( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Retrieves top countries by click count in descending order.
     * Returns Object[] arrays where [0] is country name and [1] is click count.
     * 
     * @return List of country-click count pairs ordered by popularity
     */
    @Query(QueryConstants.FIND_TOP_COUNTRIES_BY_CLICKS)
    List<Object[]> findTopCountriesByClicks();

    /**
     * Retrieves top cities by click count in descending order.
     * Returns Object[] arrays where [0] is city name and [1] is click count.
     * 
     * @return List of city-click count pairs ordered by popularity
     */
    @Query(QueryConstants.FIND_TOP_CITIES_BY_CLICKS)
    List<Object[]> findTopCitiesByClicks();

    /**
     * Finds all analytics records within a specified date range.
     * Useful for generating reports and analyzing traffic patterns over time.
     * 
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return List of analytics records within the specified date range
     * @throws IllegalArgumentException if startDate or endDate is null
     */
    @Query(QueryConstants.FIND_BY_ACCESSED_AT_BETWEEN)
    List<UrlAnalyticsEntity> findByAccessedAtBetween( @Param("startDate") Timestamp startDate,
                                                      @Param("endDate") Timestamp endDate );

    /**
     * Counts unique IP addresses that accessed a specific URL.
     * Helps measure unique visitor count and identify bot activity.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return Number of unique IP addresses that accessed the URL
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Query(QueryConstants.COUNT_UNIQUE_IPS_BY_SHORT_URL_KEY)
    long countUniqueIpsByShortUrlKey( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Retrieves daily click counts for a specific URL.
     * Returns Object[] arrays where [0] is date and [1] is click count.
     * Essential for daily traffic analysis and reporting.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return List of date-click count pairs ordered chronologically
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Query(QueryConstants.FIND_DAILY_CLICK_COUNTS)
    List<Object[]> findDailyClickCounts( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Retrieves hourly click counts for a specific URL.
     * Returns Object[] arrays where [0] is hour (0-23) and [1] is click count.
     * Useful for identifying peak traffic hours and optimizing resource allocation.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return List of hour-click count pairs ordered by hour
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Query(QueryConstants.FIND_HOURLY_CLICK_COUNTS)
    List<Object[]> findHourlyClickCounts( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Retrieves top referrer sources by click count in descending order.
     * Returns Object[] arrays where [0] is referrer URL and [1] is click count.
     * Helps identify effective traffic sources and marketing channels.
     * 
     * @return List of referrer-click count pairs ordered by effectiveness
     */
    @Query(QueryConstants.FIND_TOP_REFERRERS_BY_CLICKS)
    List<Object[]> findTopReferrersByClicks();

    /**
     * Retrieves top user agents by click count in descending order.
     * Returns Object[] arrays where [0] is user agent string and [1] is click count.
     * Useful for browser/device compatibility analysis and optimization.
     * 
     * @return List of user agent-click count pairs ordered by popularity
     */
    @Query(QueryConstants.FIND_TOP_USER_AGENTS_BY_CLICKS)
    List<Object[]> findTopUserAgentsByClicks();
}
