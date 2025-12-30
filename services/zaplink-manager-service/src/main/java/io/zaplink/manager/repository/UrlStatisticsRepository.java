package io.zaplink.manager.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import io.zaplink.manager.common.constants.QueryConstants;
import io.zaplink.manager.entity.UrlStatisticsEntity;

/**
 * Repository interface for URL statistics data persistence operations.
 */
@Repository
public interface UrlStatisticsRepository
    extends
    JpaRepository<UrlStatisticsEntity, Long>
{
    Optional<UrlStatisticsEntity> findByShortUrlKey( String shortUrlKey );

    boolean existsByShortUrlKey( String shortUrlKey );

    @Modifying @Query(QueryConstants.INCREMENT_TOTAL_CLICKS)
    int incrementTotalClicks( @Param("shortUrlKey") String shortUrlKey );

    @Modifying @Query(QueryConstants.INCREMENT_TODAY_CLICKS)
    int incrementTodayClicks( @Param("shortUrlKey") String shortUrlKey );

    @Modifying @Query(QueryConstants.UPDATE_LAST_ACCESSED)
    int updateLastAccessed( @Param("shortUrlKey") String shortUrlKey,
                            @Param("lastAccessed") LocalDateTime lastAccessed );

    @Modifying @Query(QueryConstants.RESET_TODAY_CLICKS)
    int resetTodayClicks();

    @Query(QueryConstants.FIND_MOST_CLICKED_URLS_STATS)
    List<UrlStatisticsEntity> findMostClickedUrls();

    @Query(QueryConstants.FIND_MOST_TODAY_CLICKED_URLS)
    List<UrlStatisticsEntity> findMostTodayClickedUrls();

    @Query(QueryConstants.FIND_RECENTLY_ACCESSED_URLS)
    List<UrlStatisticsEntity> findRecentlyAccessedUrls();

    @Query(QueryConstants.FIND_BY_LAST_ACCESSED_BETWEEN)
    List<UrlStatisticsEntity> findByLastAccessedBetween( @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate );

    @Query(QueryConstants.COUNT_URLS_WITH_TODAY_CLICKS)
    long countUrlsWithTodayClicks();

    @Query(QueryConstants.GET_TOTAL_CLICKS_ACROSS_ALL_URLS)
    Long getTotalClicksAcrossAllUrls();

    @Query(QueryConstants.GET_TODAY_CLICKS_ACROSS_ALL_URLS)
    Long getTodayClicksAcrossAllUrls();

    @Query(QueryConstants.FIND_BY_MIN_TOTAL_CLICKS)
    List<UrlStatisticsEntity> findByMinTotalClicks( @Param("minClicks") Long minClicks );

    @Query(QueryConstants.FIND_BY_MIN_TODAY_CLICKS)
    List<UrlStatisticsEntity> findByMinTodayClicks( @Param("minTodayClicks") Long minTodayClicks );

    @Query(QueryConstants.GET_CLICK_STATISTICS_SUMMARY)
    Object[] getClickStatisticsSummary();

    @Query(QueryConstants.FIND_INACTIVE_URLS)
    List<UrlStatisticsEntity> findInactiveUrls( @Param("cutoffDate") LocalDateTime cutoffDate );
}
