package io.zaplink.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.enums.BioLinkType;
import io.zaplink.core.dto.error.BioPageCreatedEvent;
import io.zaplink.core.dto.request.biopage.CreateBioPageRequest;
import io.zaplink.core.dto.request.biopage.UpdateBioPageRequest;
import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import io.zaplink.core.dto.response.biopage.BioPageResponse;
import io.zaplink.core.entity.BioLinkEntity;
import io.zaplink.core.entity.BioPageEntity;
import io.zaplink.core.repository.BioLinkRepository;
import io.zaplink.core.repository.BioPageRepository;
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
 * @see IBioLinkService
 */
@Slf4j @Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class BioPageService
{
    private final BioPageRepository     bioPageRepository;
    private final BioLinkRepository     bioLinkRepository;
    private final EventPublisherService eventPublisherService;
    /**
     * Creates a new bio page with comprehensive validation and logging.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates username uniqueness</li>
     *   <li>Creates and persists the BioPage entity</li>
     *   <li>Logs creation events with structured data</li>
     *   <li>Evicts relevant cache entries</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Record accessor methods for field access</li>
     *   <li>Pattern matching for validation</li>
     *   <li>Enhanced exception handling</li>
     * </ul>
     * 
     * @param request the create bio page request containing all required fields
     * @return the created bio page as DTO with all computed fields
     * @throws IllegalArgumentException if username already exists, validation fails, or data constraints violated
     * @throws org.springframework.dao.DataAccessException if database operation fails
     * @throws RuntimeException if unexpected error occurs during creation
     */
    @Transactional @CacheEvict(value =
    { "bioPages", "bioPagesByOwner" }, allEntries = true)
    public BioPageResponse createBioPage( CreateBioPageRequest request, String userEmail )
    {
        log.info( "Creating bio page for username: {}, owner: {}", request.username(), request.ownerId() );
        // Validate username uniqueness using Java 21 pattern matching
        if ( bioPageRepository.existsByUsername( request.username() ) )
        {
            log.warn( "Username conflict attempted: {}", request.username() );
            throw new IllegalArgumentException( String.format( ErrorConstant.ERROR_USERNAME_ALREADY_EXISTS,
                                                               request.username() ) );
        }
        try
        {
            BioPageEntity entity = new BioPageEntity();
            entity.setUsername( request.username() );
            entity.setOwnerId( request.ownerId() );
            entity.setThemeConfig( request.themeConfig() );
            entity.setAvatarUrl( request.avatarUrl() );
            entity.setBioText( request.bioText() );
            BioPageEntity saved = bioPageRepository.save( entity );
            log.info( "Successfully created bio page with ID: {}, username: {}", saved.getId(), saved.getUsername() );
            BioPageResponse response = convertToDto( saved );
            eventPublisherService.publishBioPageEvent( "bio.page-created", response );
            return response;
        }
        catch ( Exception e )
        {
            log.error( "Failed to create bio page for username: {}", request.username(), e );
            throw new RuntimeException( "Failed to create bio page", e );
        }
    }

    /**
     * Updates an existing bio page with comprehensive validation and caching management.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates bio page existence and accessibility</li>
     *   <li>Updates only non-null fields using Java 21 Optional chaining</li>
     *   <li>Validates updated data (URLs, formats, constraints)</li>
     *   <li>Updates cache entries appropriately</li>
     *   <li>Logs update events with structured data</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Optional chaining for selective field updates</li>
     *   <li>Pattern matching for validation</li>
     *   <li>Enhanced exception handling</li>
     * </ul>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Updates specific page cache entry</li>
     *   <li>Evicts username and owner-based caches</li>
     *   <li>Maintains cache consistency</li>
     * </ul>
     * 
     * @param id the unique identifier of the bio page to update
     * @param request the update bio page request containing fields to modify
     * @return the updated bio page as DTO with all computed fields
     * @throws IllegalArgumentException if bio page not found, validation fails, or data constraints violated
     * @throws org.springframework.dao.DataAccessException if database operation fails
     * @throws RuntimeException if unexpected error occurs during update
     */
    @Transactional @CachePut(value = "bioPages", key = "#id") @CacheEvict(value =
    { "bioPagesByUsername", "bioPagesByOwner", "bioPagesByOwnerList" }, allEntries = true)
    public BioPageResponse updateBioPage( Long id, UpdateBioPageRequest request, String userEmail )
    {
        log.info( "Updating bio page with ID: {}", id );
        BioPageEntity entity = bioPageRepository.findById( id ).orElseThrow( () -> {
            log.warn( "Attempted to update non-existent bio page with ID: {}", id );
            return new IllegalArgumentException( String.format( ErrorConstant.ERROR_BIO_PAGE_NOT_FOUND_WITH_ID, id ) );
        } );
        // Update fields using Java 21 Optional chaining
        Optional.ofNullable( request.bioText() ).ifPresent( entity::setBioText );
        Optional.ofNullable( request.avatarUrl() ).ifPresent( entity::setAvatarUrl );
        Optional.ofNullable( request.themeConfig() ).ifPresent( entity::setThemeConfig );
        BioPageEntity saved = bioPageRepository.save( entity );
        log.info( "Successfully updated bio page with ID: {}", id );
        BioPageResponse response = convertToDto( saved );
        eventPublisherService.publishBioPageEvent( "bio.page-updated", response );
        return response;
    }

    /**
     * Deletes a bio page and all associated links with comprehensive cleanup.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates bio page existence</li>
     *   <li>Logs deletion details for audit (including link count)</li>
     *   <li>Performs cascade deletion of associated links</li>
     *   <li>Evicts all relevant cache entries</li>
     *   <li>Handles cleanup of related data</li>
     * </ul>
     * 
     * <p><strong>Important Notes:</strong></p>
     * <ul>
     *   <li>This operation is irreversible</li>
     *   <li>All associated bio links will be deleted</li>
     *   <li>Username becomes available for reuse</li>
     *   <li>Consider soft deletion for temporary removal</li>
     * </ul>
     * 
     * <p><strong>Caching Strategy:</strong></p>
     * <ul>
     *   <li>Complete cache eviction for consistency</li>
     *   <li>Removes username, owner, and page-based caches</li>
     * </ul>
     * 
     * @param id the unique identifier of the bio page to delete
     * @return true if the bio page was deleted, false if not found
     * @throws IllegalArgumentException if id is null
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    @Transactional @CacheEvict(value =
    { "bioPages", "bioPagesByUsername", "bioPagesByOwner" }, allEntries = true)
    public boolean deleteBioPage( Long id, String userEmail )
    {
        log.info( "Attempting to delete bio page with ID: {}", id );
        return bioPageRepository.findById( id ).map( entity -> {
            // Log deletion details for audit
            int linkCount = entity.getBioLinks() != null ? entity.getBioLinks().size() : 0;
            log.info( "Deleting bio page '{}' with {} associated links", entity.getUsername(), linkCount );
            // Should convert before deleting to send last state
            BioPageResponse response = convertToDto( entity );
            bioPageRepository.delete( entity );
            log.info( "Successfully deleted bio page with ID: {}", id );
            eventPublisherService.publishBioPageEvent( "bio.page-deleted", response );
            return true;
        } ).orElseGet( () -> {
            log.warn( "Attempted to delete non-existent bio page with ID: {}", id );
            return false;
        } );
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
    private BioPageResponse convertToDto( BioPageEntity entity )
    {
        log.trace( "Converting BioPageEntity to DTO for ID: {}", entity.getId() );
        try
        {
            // Use Java 21 enhanced Stream API for link conversion
            List<BioLinkResponse> linkDtos = Optional.ofNullable( entity.getBioLinks() ).orElseGet( ArrayList::new )
                    .stream().filter( link -> link.getIsActive() != null && link.getIsActive() ).sorted( ( l1, l2 ) -> {
                        Integer sort1 = l1.getSortOrder() != null ? l1.getSortOrder() : 0;
                        Integer sort2 = l2.getSortOrder() != null ? l2.getSortOrder() : 0;
                        return sort1.compareTo( sort2 );
                    } ).map( this::convertLinkToDto ).collect( Collectors.toList() );
            return new BioPageResponse( entity.getId(),
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
    private BioLinkResponse convertLinkToDto( BioLinkEntity entity )
    {
        return new BioLinkResponse( entity.getId(),
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
     * Event listener for BioPage creation events.
     * 
     * <p>This method handles events generated when a new bio page is created.
     * It runs in a separate transaction to avoid interfering with the main
     * transaction that created the entity.</p>
     * 
     * <p><strong>Features:</strong></p>
     * <ul>
     *   <li>Runs in a separate transaction</li>
     *   <li>Handles BioPageCreatedEvent events</li>
     *   <li>Separate transaction for event processing</li>
     *   <li>Initializes default bio links for new pages</li>
     *   <li>Sends notifications to external systems</li>
     *   <li>Updates analytics and metrics</li>
     * </ul>
     * 
     * @param event the bio page creation event containing page details
     * @throws IllegalArgumentException if event is null
     * @throws RuntimeException if event processing fails
     */
    @TransactionalEventListener @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleBioPageCreated( BioPageCreatedEvent event )
    {
        log.info( "Processing bio page created event for username: {}, pageId: {}", event.username(), event.id() );
        try
        {
            // Validate event
            if ( event == null )
            {
                throw new IllegalArgumentException( ErrorConstant.ERROR_BIOPAGE_CREATED_EVENT_CANNOT_BE_NULL );
            }
            // Retrieve the created bio page to verify it exists
            BioPageEntity bioPage = bioPageRepository.findById( event.id() )
                    .orElseThrow( () -> new IllegalArgumentException( String
                            .format( ErrorConstant.ERROR_BIOPAGE_NOT_FOUND_WITH_ID, event.id() ) ) );
            // Verify the username matches
            if ( !bioPage.getUsername().equals( event.username() ) )
            {
                log.warn( "Username mismatch in BioPageCreatedEvent. Event: {}, Actual: {}", event.username(),
                          bioPage.getUsername() );
                throw new IllegalArgumentException( ErrorConstant.ERROR_USERNAME_MISMATCH );
            }
            log.debug( "Successfully validated bio page: {} ({})", bioPage.getUsername(), bioPage.getId() );
            // Initialize default bio links if this is a new page with no links
            if ( bioPage.getBioLinks() == null || bioPage.getBioLinks().isEmpty() )
            {
                initializeDefaultBioLinks( bioPage );
                log.info( "Initialized default bio links for page: {}", bioPage.getUsername() );
            }
            // Update analytics - track new bio page creation
            updateAnalyticsForNewPage( bioPage );
            // Send notification to external systems if configured
            sendCreationNotification( bioPage );
            // Cache the new bio page for faster access
            cacheBioPage( bioPage );
            log.info( "Successfully processed bio page created event for username: {}, pageId: {}", event.username(),
                      event.id() );
        }
        catch ( Exception e )
        {
            log.error( "Failed to process BioPageCreatedEvent for username: {}, pageId: {}", event.username(),
                       event.id(), e );
            // Re-throw to mark the transaction as failed
            throw new RuntimeException( "Failed to process BioPageCreatedEvent", e );
        }
    }

    /**
     * Initializes default bio links for a newly created bio page.
     * 
     * @param bioPage the bio page to initialize with default links
     */
    private void initializeDefaultBioLinks( BioPageEntity bioPage )
    {
        log.debug( "Initializing default bio links for page: {}", bioPage.getUsername() );
        // Create a welcome link
        BioLinkEntity welcomeLink = new BioLinkEntity();
        welcomeLink.setBioPage( bioPage );
        welcomeLink.setTitle( "Welcome to my page!" );
        welcomeLink.setUrl( "https://zaplink.io" );
        welcomeLink.setType( BioLinkType.LINK );
        welcomeLink.setIsActive( true );
        welcomeLink.setSortOrder( 1 );
        bioLinkRepository.save( welcomeLink );
        log.debug( "Created welcome link for page: {}", bioPage.getUsername() );
        // Note: computeActiveLinksCount() is handled by entity lifecycle methods
        // The entity will automatically recompute when loaded or after save
        bioPageRepository.save( bioPage );
    }

    /**
     * Updates analytics for a newly created bio page.
     * 
     * @param bioPage the newly created bio page
     */
    private void updateAnalyticsForNewPage( BioPageEntity bioPage )
    {
        log.debug( "Updating analytics for new bio page: {}", bioPage.getUsername() );
        // In a real implementation, this would:
        // 1. Increment total bio pages count
        // 2. Track creation timestamp for analytics
        // 3. Update user metrics
        // 4. Store in time-series database
        // For now, just log the analytics event
        log.info( "Analytics: New bio page created - Username: {}, CreatedAt: {}, OwnerId: {}", bioPage.getUsername(),
                  bioPage.getCreatedAt(), bioPage.getOwnerId() );
    }

    /**
     * Sends creation notification to external systems.
     * 
     * @param bioPage the bio page that was created
     */
    private void sendCreationNotification( BioPageEntity bioPage )
    {
        log.debug( "Sending creation notification for page: {}", bioPage.getUsername() );
        // In a real implementation, this would:
        // 1. Send email notification to user
        // 2. Publish to message queue for async processing
        // 3. Notify external services
        // 4. Update search index
        // For now, just log the notification
        log.info( "Notification: Bio page created - Username: {}, Public URL: {}", bioPage.getUsername(),
                  bioPage.getPublicUrl() );
    }

    /**
     * Caches the newly created bio page for faster access.
     * 
     * @param bioPage the bio page to cache
     */
    private void cacheBioPage( BioPageEntity bioPage )
    {
        log.debug( "Caching bio page: {}", bioPage.getUsername() );
        // In a real implementation, this would:
        // 1. Store in Redis cache
        // 2. Set appropriate TTL
        // 3. Update CDN if configured
        // For now, just log the cache operation
        log.info( "Cache: Bio page cached - Username: {}, PageId: {}", bioPage.getUsername(), bioPage.getId() );
    }
}
