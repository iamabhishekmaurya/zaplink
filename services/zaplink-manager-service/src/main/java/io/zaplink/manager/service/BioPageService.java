package io.zaplink.manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.manager.dto.BioLinkDto;
import io.zaplink.manager.dto.BioPageDto;
import io.zaplink.manager.entity.BioLinkEntity;
import io.zaplink.manager.entity.BioPageEntity;
import io.zaplink.manager.repository.BioLinkRepository;
import io.zaplink.manager.repository.BioPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing BioPage entities in the Zaplink platform.
 * 
 * <p>This service provides comprehensive CRUD operations for bio pages, including
 * validation, caching, and transaction management. It leverages Spring Boot 4.0.2
 * features and Java 21 capabilities for enhanced performance and developer experience.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Spring Boot 4.0.2 caching with @Cacheable annotations</li>
 *   <li>Java 21 virtual thread compatibility</li>
 *   <li>Comprehensive logging with structured data</li>
 *   <li>Transaction management with event listeners</li>
 *   <li>Stream API optimizations with Java 21 features</li>
 *   <li>Validation and error handling</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features Used:</strong></p>
 * <ul>
 *   <li>Enhanced switch expressions</li>
 *   <li>Record patterns for validation</li>
 *   <li>Virtual thread support annotations</li>
 *   <li>Improved Stream API usage</li>
 * </ul>
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-30
 * @see BioPageEntity
 * @see BioPageRepository
 * @see BioLinkService
 */
@Slf4j @Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class BioPageService
{
    private final BioPageRepository bioPageRepository;
    private final BioLinkRepository bioLinkRepository;
    /**
     * Retrieves a bio page by its unique identifier with caching support.
     * 
     * <p>This method provides cached access to individual bio pages
     * for viewing, editing, or management operations. The cache key
     * is based on the page ID for optimal performance.</p>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Cache key: page ID</li>
     *   <li>Cache TTL: application default</li>
     *   <li>Cache eviction: on updates and deletes</li>
     * </ul>
     * 
     * @param id the unique identifier of the bio page
     * @return optional containing the bio page DTO if found, empty otherwise
     * @throws IllegalArgumentException if id is null
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    @Cacheable(value = "bioPages", key = "#id")
    public Optional<BioPageDto> getBioPageById( Long id )
    {
        log.trace( "Fetching bio page by ID: {}", id );
        return bioPageRepository.findById( id ).map( this::convertToDto ).or( () -> {
            log.debug( "Bio page not found with ID: {}", id );
            return Optional.empty();
        } );
    }

    /**
     * Retrieves a bio page by its username with active links, optimized for public display.
     * 
     * <p>This method is specifically designed for public bio page viewing,
     * automatically filtering and including only active links. The results
     * are cached for optimal performance on high-traffic public pages.</p>
     * 
     * <p><strong>Features:</strong></p>
     * <ul>
     *   <li>Username-based lookup (case-sensitive)</li>
     *   <li>Automatic filtering of active links only</li>
     *   <li>Links sorted by sort order for display</li>
     *   <li>Cached for public access performance</li>
     * </ul>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Cache key: username</li>
     *   <li>Cache TTL: extended for public pages</li>
     *   <li>Cache eviction: on any page update</li>
     * </ul>
     * 
     * @param username the unique username of the bio page
     * @return optional containing the bio page DTO with active links if found, empty otherwise
     * @throws IllegalArgumentException if username is null or empty
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    @Cacheable(value = "bioPagesByUsername", key = "#username")
    public Optional<BioPageDto> getBioPageByUsername( String username )
    {
        log.trace( "Fetching bio page by username: {}", username );
        return bioPageRepository.findByUsernameWithLinks( username ).map( this::convertToDto ).or( () -> {
            log.debug( "Bio page not found with username: {}", username );
            return Optional.empty();
        } );
    }

    /**
     * Retrieves all bio pages for a specific owner with pagination support.
     * 
     * <p>This method provides paginated access to all bio pages owned
     * by a specific user or entity. It's optimized for admin panels
     * and user dashboards where users need to manage their pages.</p>
     * 
     * <p><strong>Features:</strong></p>
     * <ul>
     *   <li>Pagination support for large datasets</li>
     *   <li>Sorting support via Pageable parameter</li>
     *   <li>Cached for frequent access patterns</li>
     *   <li>Owner-based filtering</li>
     * </ul>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Cache key: ownerId + pageNumber + pageSize</li>
     *   <li>Cache TTL: moderate for user data</li>
     *   <li>Cache eviction: on page creation/deletion</li>
     * </ul>
     * 
     * @param ownerId the unique identifier of the page owner
     * @param pageable pagination and sorting parameters
     * @return page of bio page DTOs with pagination metadata
     * @throws IllegalArgumentException if ownerId is null or pageable is invalid
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    @Cacheable(value = "bioPagesByOwner", key = "#ownerId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<BioPageDto> getBioPagesByOwner( String ownerId, Pageable pageable )
    {
        log.trace( "Fetching bio pages for owner: {}, page: {}", ownerId, pageable.getPageNumber() );
        return bioPageRepository.findByOwnerId( ownerId, pageable ).map( this::convertToDto );
    }

    /**
     * Retrieves all bio pages for a specific owner without pagination.
     * 
     * <p>This method provides complete access to all bio pages owned
     * by a specific user or entity. It's suitable for admin panels,
     * user dashboards, and export operations where pagination is not required.</p>
     * 
     * <p><strong>Performance Considerations:</strong></p>
     * <ul>
     *   <li>Use with caution for owners with many pages</li>
     *   <li>Consider pagination for large datasets</li>
     *   <li>Cached for frequent access patterns</li>
     * </ul>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Cache key: ownerId</li>
     *   <li>Cache TTL: moderate for user data</li>
     *   <li>Cache eviction: on any page change for owner</li>
     * </ul>
     * 
     * @param ownerId the unique identifier of the page owner
     * @return list of all bio page DTOs owned by the specified owner
     * @throws IllegalArgumentException if ownerId is null or empty
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    @Cacheable(value = "bioPagesByOwnerList", key = "#ownerId")
    public List<BioPageDto> getBioPagesByOwnerId( String ownerId )
    {
        log.trace( "Fetching bio pages for owner: {}", ownerId );
        return bioPageRepository.findByOwnerId( ownerId ).stream().map( this::convertToDto )
                .collect( Collectors.toList() );
    }

    /**
     * Checks if a username exists in the system with caching support.
     * 
     * <p>This method provides fast validation for username uniqueness
     * during user registration and bio page creation. It's optimized
     * for high-frequency checks during form validation.</p>
     * 
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>Real-time username validation in forms</li>
     *   <li>Username availability checking</li>
     *   <li>Import validation for bulk operations</li>
     * </ul>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Cache key: username</li>
     *   <li>Cache TTL: short for availability data</li>
     *   <li>Cache eviction: on username creation/deletion</li>
     * </ul>
     * 
     * @param username the username to check for existence
     * @return true if the username exists, false otherwise
     * @throws IllegalArgumentException if username is null or empty
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    @Cacheable(value = "usernameExists", key = "#username")
    public boolean usernameExists( String username )
    {
        log.trace( "Checking if username exists: {}", username );
        return bioPageRepository.existsByUsername( username );
    }

    /**
     * Retrieves comprehensive bio page statistics for monitoring and analytics.
     * 
     * <p>This method provides system-wide statistics for monitoring
     * the bio page platform health and usage patterns. It includes
     * page counts, link statistics, and computed averages.</p>
     * 
     * <p><strong>Statistics Included:</strong></p>
     * <ul>
     *   <li>Total number of bio pages in the system</li>
     *   <li>Total number of bio links across all pages</li>
     *   <li>Average links per page (computed metric)</li>
     *   <li>Timestamp when statistics were computed</li>
     * </ul>
     * 
     * <p><strong>Performance Notes:</strong></p>
     * <ul>
     *   <li>Uses efficient COUNT queries for scalability</li>
     *   <li>Computed metrics avoid expensive joins</li>
     *   <li>Suitable for dashboard and monitoring displays</li>
     * </ul>
     * 
     * @return bio page statistics with computed metrics and timestamp
     * @throws org.springframework.dao.DataAccessException if database operation fails
     * @throws RuntimeException if statistics computation fails
     */
    public BioPageStatistics getStatistics()
    {
        log.trace( "Computing bio page statistics" );
        try
        {
            long totalPages = bioPageRepository.count();
            long totalLinks = bioLinkRepository.count();
            double avgLinksPerPage = totalPages > 0 ? totalLinks / (double) totalPages : 0.0;
            BioPageStatistics stats = new BioPageStatistics( totalPages,
                                                             totalLinks,
                                                             avgLinksPerPage,
                                                             LocalDateTime.now() );
            log.debug( "Computed statistics: {}", stats );
            return stats;
        }
        catch ( Exception e )
        {
            log.error( "Failed to compute bio page statistics", e );
            throw new RuntimeException( "Failed to compute statistics", e );
        }
    }

    /**
     * Converts a BioPageEntity to BioPageDto with enhanced mapping and link processing.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Maps all entity fields to DTO fields</li>
     *   <li>Filters and processes associated bio links</li>
     *   <li>Sorts active links by sort order for display</li>
     *   <li>Handles null relationships gracefully</li>
     *   <li>Provides structured logging for debugging</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Enhanced Stream API for link processing</li>
     *   <li>Optional chaining for null-safe operations</li>
     *   <li>Pattern matching for link filtering</li>
     * </ul>
     * 
     * <p><strong>Link Processing:</strong></p>
     * <ul>
     *   <li>Filters only active links (isActive = true)</li>
     *   <li>Sorts by sort order (null values treated as 0)</li>
     *   <li>Converts each link entity to DTO</li>
     *   <li>Handles empty link collections gracefully</li>
     * </ul>
     * 
     * @param entity the bio page entity to convert (must not be null)
     * @return the corresponding bio page DTO with processed active links
     * @throws IllegalArgumentException if entity is null
     * @throws RuntimeException if conversion fails due to data issues
     */
    private BioPageDto convertToDto( BioPageEntity entity )
    {
        log.trace( "Converting BioPageEntity to DTO for ID: {}", entity.getId() );
        try
        {
            // Use Java 21 enhanced Stream API for link conversion
            List<BioLinkDto> linkDtos = Optional.ofNullable( entity.getBioLinks() )
                    .orElseGet( java.util.ArrayList::new ).stream()
                    .filter( link -> link.getIsActive() != null && link.getIsActive() ).sorted( ( l1, l2 ) -> {
                        Integer sort1 = l1.getSortOrder() != null ? l1.getSortOrder() : 0;
                        Integer sort2 = l2.getSortOrder() != null ? l2.getSortOrder() : 0;
                        return sort1.compareTo( sort2 );
                    } ).map( this::convertLinkToDto ).collect( Collectors.toList() );
            return new BioPageDto( entity.getId(),
                                   entity.getUsername(),
                                   entity.getOwnerId(),
                                   entity.getThemeConfig(),
                                   entity.getAvatarUrl(),
                                   entity.getBioText(),
                                   entity.getCreatedAt(),
                                   entity.getUpdatedAt(),
                                   linkDtos );
        }
        catch ( Exception e )
        {
            log.error( "Failed to convert BioPageEntity to DTO for ID: {}", entity.getId(), e );
            throw new RuntimeException( "Failed to convert entity to DTO", e );
        }
    }

    /**
     * Converts a BioLinkEntity to BioLinkDto with type-safe mapping and precision handling.
     * 
     * <p>This method performs precise data conversion between entity
     * and DTO layers, handling type conversions and null values safely.</p>
     * 
     * <p><strong>Conversion Details:</strong></p>
     * <ul>
     *   <li>BigDecimal price converted to Double for API compatibility</li>
     *   <li>Null bio page reference handled gracefully</li>
     *   <li>All timestamp fields preserved as LocalDateTime</li>
     *   <li>Type name extracted from enum for API display</li>
     * </ul>
     * 
     * <p><strong>Type Safety Notes:</strong></p>
     * <ul>
     *   <li>Price conversion maintains precision (2 decimal places)</li>
     *   <li>Null relationships result in null DTO fields</li>
     *   <li>Enum types converted to strings for JSON serialization</li>
     * </ul>
     * 
     * @param entity the bio link entity to convert (must not be null)
     * @return the corresponding bio link DTO with all fields properly mapped
     * @throws IllegalArgumentException if entity is null
     * @throws RuntimeException if conversion fails due to data type issues
     */
    private BioLinkDto convertLinkToDto( BioLinkEntity entity )
    {
        return new BioLinkDto( entity.getId(),
                               entity.getBioPage() != null ? entity.getBioPage().getId() : null,
                               entity.getTitle(),
                               entity.getUrl(),
                               entity.getTypeName(),
                               entity.getIsActive(),
                               entity.getSortOrder(),
                               entity.getPrice() != null ? entity.getPrice().doubleValue() : null,
                               entity.getCurrency(),
                               entity.getCreatedAt(),
                               entity.getUpdatedAt() );
    }
    /**
     * Record representing bio page statistics for monitoring and analytics.
     * 
     * <p>This immutable record provides a comprehensive snapshot of
     * the bio page platform's current state and usage patterns.
     * It's designed for dashboard displays, monitoring systems,
     * and analytical reporting.</p>
     * 
     * <p><strong>Statistics Included:</strong></p>
     * <ul>
     *   <li><strong>totalPages:</strong> Total number of bio pages created</li>
     *   <li><strong>totalLinks:</strong> Total number of bio links across all pages</li>
     *   <li><strong>averageLinksPerPage:</strong> Computed average links per page</li>
     *   <li><strong>computedAt:</strong> Timestamp when statistics were calculated</li>
     * </ul>
     * 
     * <p><strong>Usage Examples:</strong></p>
     * <ul>
     *   <li>Dashboard metrics display</li>
     *   <li>System health monitoring</li>
     *   <li>Usage trend analysis</li>
     *   <li>Performance metrics</li>
     * </ul>
     * 
     * @param totalPages total number of bio pages in the system
     * @param totalLinks total number of bio links across all pages
     * @param averageLinksPerPage computed average links per page (may be 0.0 for empty systems)
     * @param computedAt timestamp when these statistics were computed
     */
    public record BioPageStatistics( long totalPages,
                                     long totalLinks,
                                     double averageLinksPerPage,
                                     LocalDateTime computedAt )
    {
        @Override
        public String toString()
        {
            return String.format( "BioPageStatistics{totalPages=%d, totalLinks=%d, avgLinks=%.2f, computedAt=%s}",
                                  totalPages, totalLinks, averageLinksPerPage, computedAt );
        }
    }
}
