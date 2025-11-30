package io.zaplink.manager.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.constants.QueryConstants;
import io.zaplink.manager.entity.UrlStatisticsEntity;

/**
 * Repository interface for URL statistics data persistence operations.
 * Manages aggregated click statistics, performance metrics, and analytics data
 * for reporting and dashboard functionality.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface UrlStatisticsRepository
    extends
    JpaRepository<UrlStatisticsEntity, BigInteger>
{
    /**
     * Retrieves statistics for a specific URL by its short URL key.
     * Primary method for accessing URL performance metrics.
     * 
     * @param shortUrlKey the unique identifier for the shortened URL
     * @return Optional containing statistics if found, empty otherwise
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    Optional<UrlStatisticsEntity> findByShortUrlKey( String shortUrlKey );

    /**
     * Checks if statistics exist for a specific short URL key.
     * Used to determine if statistics need to be initialized.
     * 
     * @param shortUrlKey the unique identifier to check for statistics existence
     * @return true if statistics exist, false otherwise
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    boolean existsByShortUrlKey( String shortUrlKey );

    /**
     * Increments the total click count for a specific URL.
     * Atomically updates the cumulative click counter.
     * 
     * @param shortUrlKey the unique identifier for the URL to update
     * @return Number of rows affected (should be 1 if successful, 0 if not found)
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Modifying @Query(QueryConstants.INCREMENT_TOTAL_CLICKS)
    int incrementTotalClicks( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Increments the daily click count for a specific URL.
     * Used for tracking daily performance metrics.
     * 
     * @param shortUrlKey the unique identifier for the URL to update
     * @return Number of rows affected (should be 1 if successful, 0 if not found)
     * @throws IllegalArgumentException if shortUrlKey is null or empty
     */
    @Modifying @Query(QueryConstants.INCREMENT_TODAY_CLICKS)
    int incrementTodayClicks( @Param("shortUrlKey") String shortUrlKey );

    /**
     * Updates the last accessed timestamp for a specific URL.
     * Tracks when the URL was most recently accessed.
     * 
     * @param shortUrlKey the unique identifier for the URL to update
     * @param lastAccessed the timestamp of the last access
     * @return Number of rows affected (should be 1 if successful, 0 if not found)
     * @throws IllegalArgumentException if shortUrlKey or lastAccessed is null
     */
    @Modifying @Query(QueryConstants.UPDATE_LAST_ACCESSED)
    int updateLastAccessed( @Param("shortUrlKey") String shortUrlKey, @Param("lastAccessed") Timestamp lastAccessed );

    /**
     * Resets all daily click counts to zero.
     * Typically called at midnight to start a new day's tracking.
     * 
     * @return Number of rows affected (all statistics records)
     */
    @Modifying @Query(QueryConstants.RESET_TODAY_CLICKS)
    int resetTodayClicks();

    /**
     * Retrieves URLs ordered by total click count in descending order.
     * Returns the most popular URLs based on all-time performance.
     * 
     * @return List of statistics ordered by total popularity
     */
    @Query(QueryConstants.FIND_MOST_CLICKED_URLS_STATS)
    List<UrlStatisticsEntity> findMostClickedUrls();

    /**
     * Retrieves URLs ordered by today's click count in descending order.
     * Returns today's most popular URLs for daily performance tracking.
     * 
     * @return List of statistics ordered by today's popularity
     */
    @Query(QueryConstants.FIND_MOST_TODAY_CLICKED_URLS)
    List<UrlStatisticsEntity> findMostTodayClickedUrls();

    /**
     * Retrieves URLs ordered by last accessed time in descending order.
     * Returns recently accessed URLs for activity monitoring.
     * 
     * @return List of statistics ordered by most recent access
     */
    @Query(QueryConstants.FIND_RECENTLY_ACCESSED_URLS)
    List<UrlStatisticsEntity> findRecentlyAccessedUrls();

    /**
     * Finds URLs that were accessed within a specific date range.
     * Useful for analyzing recent activity patterns.
     * 
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return List of statistics for URLs accessed in the date range
     * @throws IllegalArgumentException if startDate or endDate is null
     */
    @Query(QueryConstants.FIND_BY_LAST_ACCESSED_BETWEEN)
    List<UrlStatisticsEntity> findByLastAccessedBetween( @Param("startDate") Timestamp startDate,
                                                         @Param("endDate") Timestamp endDate );

    /**
     * Counts URLs that have received clicks today.
     * Provides insight into daily URL usage patterns.
     * 
     * @return Number of URLs with today's click count greater than zero
     */
    @Query(QueryConstants.COUNT_URLS_WITH_TODAY_CLICKS)
    long countUrlsWithTodayClicks();

    /**
     * Calculates the total clicks across all URLs.
     * Provides overall system usage statistics.
     * 
     * @return Total number of clicks across all URLs, or null if no data
     */
    @Query(QueryConstants.GET_TOTAL_CLICKS_ACROSS_ALL_URLS)
    Long getTotalClicksAcrossAllUrls();

    /**
     * Calculates today's total clicks across all URLs.
     * Provides daily system usage statistics.
     * 
     * @return Total number of clicks today across all URLs, or null if no data
     */
    @Query(QueryConstants.GET_TODAY_CLICKS_ACROSS_ALL_URLS)
    Long getTodayClicksAcrossAllUrls();

    /**
     * Finds URLs with total click counts greater than or equal to a minimum threshold.
     * Useful for filtering popular URLs for analytics and reporting.
     * 
     * @param minClicks the minimum number of total clicks required
     * @return List of statistics for URLs meeting the minimum click threshold
     * @throws IllegalArgumentException if minClicks is null or negative
     */
    @Query(QueryConstants.FIND_BY_MIN_TOTAL_CLICKS)
    List<UrlStatisticsEntity> findByMinTotalClicks( @Param("minClicks") Long minClicks );

    /**
     * Finds URLs with today's click counts greater than or equal to a minimum threshold.
     * Useful for identifying URLs with significant daily activity.
     * 
     * @param minTodayClicks the minimum number of today's clicks required
     * @return List of statistics for URLs meeting the daily minimum threshold
     * @throws IllegalArgumentException if minTodayClicks is null or negative
     */
    @Query(QueryConstants.FIND_BY_MIN_TODAY_CLICKS)
    List<UrlStatisticsEntity> findByMinTodayClicks( @Param("minTodayClicks") Long minTodayClicks );

    /**
     * Retrieves comprehensive click statistics summary across all URLs.
     * Returns aggregate statistics including count, sum, average, and maximum clicks.
     * 
     * @return Object array containing: [0] total URLs, [1] total clicks, [2] average clicks, [3] maximum clicks
     */
    @Query(QueryConstants.GET_CLICK_STATISTICS_SUMMARY)
    Object[] getClickStatisticsSummary();

    /**
     * Finds URLs that have not been accessed since a specific cutoff date.
     * Useful for identifying inactive URLs for cleanup or analysis.
     * 
     * @param cutoffDate the date before which URLs are considered inactive
     * @return List of statistics for inactive URLs
     * @throws IllegalArgumentException if cutoffDate is null
     */
    @Query(QueryConstants.FIND_INACTIVE_URLS)
    List<UrlStatisticsEntity> findInactiveUrls( @Param("cutoffDate") Timestamp cutoffDate );
}
