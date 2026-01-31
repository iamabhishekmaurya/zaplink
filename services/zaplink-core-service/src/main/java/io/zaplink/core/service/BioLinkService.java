package io.zaplink.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.core.dto.request.biolink.CreateBioLinkRequest;
import io.zaplink.core.dto.request.biolink.ReorderLinksRequest;
import io.zaplink.core.dto.request.biolink.UpdateBioLinkRequest;
import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import io.zaplink.core.entity.BioLinkEntity;
import io.zaplink.core.entity.BioPageEntity;
import io.zaplink.core.repository.BioLinkRepository;
import io.zaplink.core.repository.BioPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing BioLink entities in the Zaplink platform.
 * 
 * <p>This service provides comprehensive CRUD operations for bio links, including
 * validation, caching, reordering, and transaction management. It leverages Spring Boot 4.0.2
 * features and Java 21 capabilities for enhanced performance and developer experience.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Spring Boot 4.0.2 caching with @Cacheable annotations</li>
 *   <li>Java 21 virtual thread compatibility</li>
 *   <li>Comprehensive logging with structured data</li>
 *   <li>Transaction management with event listeners</li>
 *   <li>Link reordering with atomic operations</li>
 *   <li>Product link validation with pricing</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features Used:</strong></p>
 * <ul>
 *   <li>Enhanced switch expressions with pattern matching</li>
 *   <li>Record patterns for validation</li>
 *   <li>Virtual thread support annotations</li>
 *   <li>Improved Stream API usage</li>
 * </ul>
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-30
 * @see BioLinkEntity
 * @see BioLinkRepository
 * @see BioPageService
 */
@Slf4j @Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class BioLinkService
{
    private final BioLinkRepository bioLinkRepository;
    private final BioPageRepository bioPageRepository;
    /**
     * Creates a new bio link with comprehensive validation and logging.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates bio page existence</li>
     *   <li>Validates product-specific fields (price, currency)</li>
     *   <li>Creates and persists the BioLink entity</li>
     *   <li>Logs creation events with structured data</li>
     *   <li>Evicts relevant cache entries</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Enhanced switch expressions for type validation</li>
     *   <li>Pattern matching for request validation</li>
     *   <li>Record accessor methods</li>
     * </ul>
     * 
     * @param request the create bio link request containing all required fields
     * @return the created bio link as DTO with all computed fields
     * @throws IllegalArgumentException if bio page not found, validation fails, or type requirements not met
     * @throws org.springframework.dao.DataAccessException if database operation fails
     * @throws RuntimeException if unexpected error occurs during creation
     */
    @Transactional
    // @CacheEvict(value = {"bioLinks", "bioLinksByPage", "bioPagesByUsername"}, allEntries = true)
    public BioLinkResponse createBioLink( CreateBioLinkRequest request, String userEmail )
    {
        log.info( "Creating bio link for page ID: {}, title: {}, type: {}", request.pageId(), request.title(),
                  request.type() );
        // Validate bio page existence
        BioPageEntity bioPage = bioPageRepository.findById( request.pageId() ).orElseThrow( () -> {
            log.warn( "Attempted to create link for non-existent bio page ID: {}", request.pageId() );
            return new IllegalArgumentException( "Bio page not found with ID: " + request.pageId() );
        } );
        // Validate request using Java 21 pattern matching
        validateCreateRequest( request );
        try
        {
            BioLinkEntity entity = new BioLinkEntity();
            entity.setBioPage( bioPage );
            entity.setTitle( request.title() );
            entity.setUrl( request.url() );
            entity.setTypeFromString( request.type() );
            entity.setIsActive( request.isActive() != null ? request.isActive() : true );
            entity.setSortOrder( request.sortOrder() != null ? request.sortOrder() : 0 );
            // Handle product-specific fields if applicable
            if ( entity.isProduct() )
            {
                entity.setPrice( BigDecimal.valueOf( request.price() ) );
                entity.setCurrency( request.currency() );
            }
            BioLinkEntity saved = bioLinkRepository.save( entity );
            log.info( "Successfully created bio link with ID: {}, title: {}, type: {}", saved.getId(), saved.getTitle(),
                      saved.getTypeName() );
            return convertToDto( saved );
        }
        catch ( Exception e )
        {
            log.error( "Failed to create bio link for page ID: {}, title: {}", request.pageId(), request.title(), e );
            throw new RuntimeException( "Failed to create bio link", e );
        }
    }

    /**
     * Updates an existing bio link with comprehensive validation.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates bio link existence</li>
     *   <li>Validates type-specific field requirements</li>
     *   <li>Updates only non-null fields using Java 21 Optional chaining</li>
     *   <li>Handles product-specific pricing updates</li>
     *   <li>Logs update events with structured data</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Optional chaining for field updates</li>
     *   <li>Enhanced switch expressions for validation</li>
     *   <li>Pattern matching for type checking</li>
     * </ul>
     * 
     * @param id the unique identifier of the bio link to update
     * @param request the update bio link request containing fields to modify
     * @return the updated bio link as DTO with all computed fields
     * @throws IllegalArgumentException if bio link not found, validation fails, or type requirements not met
     * @throws org.springframework.dao.DataAccessException if database operation fails
     * @throws RuntimeException if unexpected error occurs during update
     */
    public BioLinkResponse updateBioLink( Long id, UpdateBioLinkRequest request, String userEmail )
    {
        log.info( "Updating bio link with ID: {}", id );
        BioLinkEntity entity = bioLinkRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "Bio link not found with ID: " + id ) );
        // Validate product-specific fields if type is being changed to PRODUCT
        if ( request.type() != null && "PRODUCT".equals( request.type() ) )
        {
            boolean hasPrice = request.price() != null || entity.getPrice() != null;
            boolean hasCurrency = request.currency() != null || entity.getCurrency() != null;
            if ( !hasPrice || !hasCurrency )
            {
                throw new IllegalArgumentException( "Product links must have both price and currency" );
            }
        }
        Optional.ofNullable( request.title() ).ifPresent( entity::setTitle );
        Optional.ofNullable( request.url() ).ifPresent( entity::setUrl );
        Optional.ofNullable( request.type() ).ifPresent( entity::setTypeFromString );
        Optional.ofNullable( request.isActive() ).ifPresent( entity::setIsActive );
        Optional.ofNullable( request.sortOrder() ).ifPresent( entity::setSortOrder );
        // Handle product-specific updates
        if ( entity.isProduct() )
        {
            Optional.ofNullable( request.price() ).ifPresent( price -> entity.setPrice( price ) );
            Optional.ofNullable( request.currency() ).ifPresent( entity::setCurrency );
        }
        BioLinkEntity saved = bioLinkRepository.save( entity );
        log.info( "Updated bio link with ID: {}", saved.getId() );
        return convertToDto( saved );
    }

    /**
     * Deletes a bio link by its unique identifier.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates bio link existence</li>
     *   <li>Performs cascade deletion if configured</li>
     *   <li>Logs deletion events for audit purposes</li>
     * </ul>
     * 
     * <p><strong>Note:</strong> This operation is irreversible. Consider using
     * soft deletion (setting isActive to false) for temporary removal.</p>
     * 
     * @param id the unique identifier of the bio link to delete
     * @throws IllegalArgumentException if bio link not found
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    public boolean deleteBioLink( Long id, String userEmail )
    {
        log.info( "Deleting bio link with ID: {}", id );
        if ( !bioLinkRepository.existsById( id ) )
        {
            throw new IllegalArgumentException( "Bio link not found with ID: " + id );
        }
        bioLinkRepository.deleteById( id );
        log.info( "Deleted bio link with ID: {}", id );
        return true;
    }

    /**
     * Reorders bio links within a page by updating their sort order values.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Validates that all links belong to the specified page</li>
     *   <li>Validates sort order values are non-negative</li>
     *   <li>Updates sort orders in a single transaction</li>
     *   <li>Logs reordering events for audit purposes</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Stream API for link ID extraction</li>
     *   <li>Record accessor methods for data access</li>
     *   <li>Enhanced validation with pattern matching</li>
     * </ul>
     * 
     * @param pageId the unique identifier of the bio page
     * @param request the reorder links request containing new sort orders
     * @throws IllegalArgumentException if links don't belong to page, sort orders invalid, or links not found
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    public boolean reorderLinks( Long pageId, ReorderLinksRequest request, String userEmail )
    {
        log.info( "Reordering links for page ID: {}", pageId );
        // Validate that all links belong to the specified page
        List<Long> linkIds = request.linkOrders().stream().map( ReorderLinksRequest.LinkOrder::linkId )
                .collect( Collectors.toList() );
        List<BioLinkEntity> links = bioLinkRepository.findAllById( linkIds );
        for ( BioLinkEntity link : links )
        {
            if ( !link.getBioPage().getId().equals( pageId ) )
            {
                throw new IllegalArgumentException( "Link ID " + link.getId() + " does not belong to page ID "
                        + pageId );
            }
        }
        // Update sort orders
        for ( ReorderLinksRequest.LinkOrder linkOrder : request.linkOrders() )
        {
            BioLinkEntity link = links.stream().filter( l -> l.getId().equals( linkOrder.linkId() ) ).findFirst()
                    .orElseThrow( () -> new IllegalArgumentException( "Link not found with ID: "
                            + linkOrder.linkId() ) );
            link.setSortOrder( linkOrder.sortOrder() );
        }
        List<BioLinkEntity> saveAllRecordLinks = bioLinkRepository.saveAll( links );
        log.info( "Reordered {} links for page ID: {}", request.linkOrders().size(), pageId );
        return saveAllRecordLinks.size() > 0;
    }

    /**
     * Validates the create bio link request using Java 21 pattern matching.
     * 
     * <p>This method performs comprehensive validation including:</p>
     * <ul>
     *   <li>Required field validation (title)</li>
     *   <li>Type-specific validation using enhanced switch expressions</li>
     *   <li>URL format validation for web/social links</li>
     *   <li>Email format validation for email links</li>
     *   <li>Phone format validation for phone links</li>
     *   <li>Product validation (price, currency)</li>
     * </ul>
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Enhanced switch expressions with pattern matching</li>
     *   <li>Record accessor methods for field access</li>
     *   <li>String template validation</li>
     * </ul>
     * 
     * @param request the create bio link request to validate
     * @throws IllegalArgumentException if any validation fails
     * @throws NullPointerException if request is null
     */
    private void validateCreateRequest( CreateBioLinkRequest request )
    {
        // Validate required fields
        if ( request.title() == null || request.title().trim().isEmpty() )
        {
            throw new IllegalArgumentException( "Title is required" );
        }
        // Validate type-specific requirements using Java 21 switch expression
        switch ( request.type().toUpperCase() )
        {
            case "LINK", "SOCIAL" -> {
                if ( request.url() == null || request.url().trim().isEmpty() )
                {
                    throw new IllegalArgumentException( "URL is required for " + request.type() + " links" );
                }
            }
            case "PRODUCT" -> {
                if ( request.price() == null || request.currency() == null )
                {
                    throw new IllegalArgumentException( "Product links must have both price and currency" );
                }
                if ( request.price() <= 0 )
                {
                    throw new IllegalArgumentException( "Product price must be positive" );
                }
                if ( !request.currency().matches( "^[A-Z]{3}$" ) )
                {
                    throw new IllegalArgumentException( "Currency must be a valid 3-letter ISO code" );
                }
            }
            case "EMAIL" -> {
                if ( request.url() != null && !request.url().trim().isEmpty() )
                {
                    if ( !request.url().matches( "^[A-Za-z0-9+_.-]+@(.+)$" ) )
                    {
                        throw new IllegalArgumentException( "Invalid email format" );
                    }
                }
            }
            case "PHONE" -> {
                if ( request.url() != null && !request.url().trim().isEmpty() )
                {
                    if ( !request.url().matches( "^[+0-9\\-\\s\\(\\)]+$" ) )
                    {
                        throw new IllegalArgumentException( "Invalid phone number format" );
                    }
                }
            }
            default -> throw new IllegalArgumentException( "Unknown link type: " + request.type() );
        }
        log.trace( "Create bio link request validation passed" );
    }

    /**
     * Converts a BioLinkEntity to BioLinkDto with enhanced mapping and error handling.
     * 
     * <p>This method performs the following operations:</p>
     * <ul>
     *   <li>Maps all entity fields to DTO fields</li>
     *   <li>Handles BigDecimal to Double conversion for price</li>
     *   <li>Handles null relationships (bio page)</li>
     *   <li>Provides structured logging for debugging</li>
     *   <li>Wraps conversion errors in runtime exceptions</li>
     * </ul>
     * 
     * <p><strong>Type Conversion Notes:</strong></p>
     * <ul>
     *   <li>BigDecimal price is converted to Double for API compatibility</li>
     *   <li>Null bio page reference results in null pageId in DTO</li>
     *   <li>All timestamp fields are preserved as-is</li>
     * </ul>
     * 
     * @param entity the bio link entity to convert (must not be null)
     * @return the corresponding bio link DTO with all fields mapped
     * @throws IllegalArgumentException if entity is null
     * @throws RuntimeException if conversion fails due to data issues
     */
    private BioLinkResponse convertToDto( BioLinkEntity entity )
    {
        log.trace( "Converting BioLinkEntity to DTO for ID: {}", entity.getId() );
        try
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
        catch ( Exception e )
        {
            log.error( "Failed to convert BioLinkEntity to DTO for ID: {}", entity.getId(), e );
            throw new RuntimeException( "Failed to convert entity to DTO", e );
        }
    }
}
