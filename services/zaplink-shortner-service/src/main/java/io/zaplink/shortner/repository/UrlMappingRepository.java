package io.zaplink.shortner.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.shortner.common.constants.QueryConstants;
import io.zaplink.shortner.entity.UrlMappingEntity;

/**
 * Repository interface for URL mapping data persistence operations in the
 * processor service.
 * Handles URL creation, status management, and basic analytics integration.
 * Works in conjunction with the manager service for comprehensive URL
 * management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface UrlMappingRepository
        extends
        JpaRepository<UrlMappingEntity, Long>
{
        /**
         * Retrieves a URL mapping by its unique short URL key.
         * Primary method for URL resolution and validation during processing.
         * 
         * @param shortUrlKey the unique identifier for the shortened URL
         * @return Optional containing the URL mapping if found, empty otherwise
         * @throws IllegalArgumentException if shortUrlKey is null or empty
         */
        Optional<UrlMappingEntity> findByShortUrlKey( String shortUrlKey );

        /**
         * Checks if a URL mapping exists for the given short URL key.
         * Used for validation and duplicate prevention during URL creation.
         * 
         * @param shortUrlKey the unique identifier to check for existence
         * @return true if a mapping exists, false otherwise
         * @throws IllegalArgumentException if shortUrlKey is null or empty
         */
        boolean existsByShortUrlKey( String shortUrlKey );

        /**
         * Retrieves all URL mappings with a specific status.
         * Commonly used for finding active, inactive, or expired URLs during
         * processing.
         * 
         * @param status the status to filter by (e.g., "ACTIVE", "INACTIVE", "EXPIRED")
         * @return List of URL mappings with the specified status
         * @throws IllegalArgumentException if status is null or empty
         */
        List<UrlMappingEntity> findByStatus( String status );

        /**
         * Finds all URL mappings associated with a specific trace ID.
         * Useful for tracking and debugging URL creation requests in the processing
         * pipeline.
         * 
         * @param traceId the trace identifier to search for
         * @return List of URL mappings associated with the trace ID
         * @throws IllegalArgumentException if traceId is null or empty
         */
        List<UrlMappingEntity> findByTraceId( String traceId );

        /**
         * Counts URL mappings by their status.
         * Provides quick statistics for processing pipeline monitoring.
         * 
         * @param status the status to count
         * @return Number of URL mappings with the specified status
         * @throws IllegalArgumentException if status is null or empty
         */
        long countByStatus( String status );

        /**
         * Finds all URL mappings that have expired based on their expiration date.
         * Essential for cleanup operations and expired URL management in processing.
         * 
         * @param currentTime the current timestamp to compare against expiration dates
         * @return List of expired URL mappings
         * @throws IllegalArgumentException if currentTime is null
         */
        @Query(QueryConstants.FIND_EXPIRED_URLS)
        List<UrlMappingEntity> findExpiredUrls( @Param("currentTime") LocalDateTime currentTime );

        /**
         * Increments the click count for a specific URL mapping.
         * Atomically updates the click counter for accurate analytics during
         * processing.
         * 
         * @param shortUrlKey the unique identifier for the URL to update
         * @return Number of rows affected (should be 1 if successful, 0 if not found)
         * @throws IllegalArgumentException if shortUrlKey is null or empty
         */
        @Modifying @Query(QueryConstants.INCREMENT_CLICK_COUNT)
        int incrementClickCount( @Param("shortUrlKey") String shortUrlKey );

        /**
         * Updates the status of a URL mapping.
         * Used for activating, deactivating, or marking URLs as expired during
         * processing.
         * 
         * @param shortUrlKey the unique identifier for the URL to update
         * @param status      the new status to set
         * @return Number of rows affected (should be 1 if successful, 0 if not found)
         * @throws IllegalArgumentException if shortUrlKey or status is null/empty
         */
        @Modifying @Query(QueryConstants.UPDATE_STATUS)
        int updateStatus( @Param("shortUrlKey") String shortUrlKey, @Param("status") String status );

        /**
         * Finds URL mappings created within a specific date range.
         * Useful for analyzing URL creation patterns in the processing pipeline.
         * 
         * @param startDate the beginning of the date range (inclusive)
         * @param endDate   the end of the date range (inclusive)
         * @return List of URL mappings created within the specified date range
         * @throws IllegalArgumentException if startDate or endDate is null
         */
        @Query(QueryConstants.FIND_BY_CREATED_AT_BETWEEN)
        List<UrlMappingEntity> findByCreatedAtBetween( @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate );

        /**
         * Retrieves URL mappings ordered by click count in descending order.
         * Returns the most popular URLs first for processing analytics.
         * 
         * @return List of URL mappings ordered by popularity (most clicked first)
         */
        @Query(QueryConstants.FIND_MOST_CLICKED_URLS)
        List<UrlMappingEntity> findMostClickedUrls();
}
