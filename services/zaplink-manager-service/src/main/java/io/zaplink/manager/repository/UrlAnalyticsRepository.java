package io.zaplink.manager.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import io.zaplink.manager.common.constants.QueryConstants;
import io.zaplink.manager.entity.UrlAnalyticsEntity;

/**
 * Repository interface for URL analytics data persistence operations.
 */
@Repository
public interface UrlAnalyticsRepository
    extends
    JpaRepository<UrlAnalyticsEntity, Long>
{
    List<UrlAnalyticsEntity> findByShortUrlKey( String shortUrlKey );

    List<UrlAnalyticsEntity> findByIpAddress( String ipAddress );

    List<UrlAnalyticsEntity> findByCountry( String country );

    List<UrlAnalyticsEntity> findByCity( String city );

    List<UrlAnalyticsEntity> findByReferrer( String referrer );

    @Query(QueryConstants.COUNT_CLICKS_BY_SHORT_URL_KEY)
    long countClicksByShortUrlKey( @Param("shortUrlKey") String shortUrlKey );

    @Query(QueryConstants.COUNT_CLICKS_BY_SHORT_URL_KEY_AND_DATE_RANGE)
    long countClicksByShortUrlKeyAndDateRange( @Param("shortUrlKey") String shortUrlKey,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate );

    @Query(QueryConstants.FIND_RECENT_ANALYTICS_BY_SHORT_URL_KEY)
    List<UrlAnalyticsEntity> findRecentAnalyticsByShortUrlKey( @Param("shortUrlKey") String shortUrlKey );

    @Query(QueryConstants.FIND_TOP_COUNTRIES_BY_CLICKS)
    List<Object[]> findTopCountriesByClicks();

    @Query(QueryConstants.FIND_TOP_CITIES_BY_CLICKS)
    List<Object[]> findTopCitiesByClicks();

    @Query(QueryConstants.FIND_BY_ACCESSED_AT_BETWEEN)
    List<UrlAnalyticsEntity> findByAccessedAtBetween( @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate );

    @Query(QueryConstants.COUNT_UNIQUE_IPS_BY_SHORT_URL_KEY)
    long countUniqueIpsByShortUrlKey( @Param("shortUrlKey") String shortUrlKey );

    @Query(QueryConstants.FIND_DAILY_CLICK_COUNTS)
    List<Object[]> findDailyClickCounts( @Param("shortUrlKey") String shortUrlKey );

    @Query(QueryConstants.FIND_HOURLY_CLICK_COUNTS)
    List<Object[]> findHourlyClickCounts( @Param("shortUrlKey") String shortUrlKey );

    @Query(QueryConstants.FIND_TOP_REFERRERS_BY_CLICKS)
    List<Object[]> findTopReferrersByClicks();

    @Query(QueryConstants.FIND_TOP_USER_AGENTS_BY_CLICKS)
    List<Object[]> findTopUserAgentsByClicks();
}
