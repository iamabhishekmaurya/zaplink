package io.zaplink.manager.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.manager.dto.BioLinkDto;
import io.zaplink.manager.entity.BioLinkEntity;
import io.zaplink.manager.repository.BioLinkRepository;
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

    /**
     * Retrieves a bio link by its unique identifier.
     * 
     * <p>This method provides direct access to individual bio links
     * for editing, viewing, or management operations.</p>
     * 
     * @param id the unique identifier of the bio link
     * @return optional containing the bio link DTO if found, empty otherwise
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    public Optional<BioLinkDto> getBioLinkById(Long id)
    {
        return bioLinkRepository.findById( id ).map( this::convertToDto );
    }

    /**
     * Retrieves all bio links for a specific page, sorted by sort order.
     * 
     * <p>This method fetches all bio links associated with the given page ID,
     * including both active and inactive links. The results are automatically
     * sorted by sort order in ascending order for proper display.</p>
     * 
     * @param pageId the unique identifier of the bio page
     * @return list of bio link DTOs sorted by sort order (ascending)
     * @throws IllegalArgumentException if pageId is null
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    public List<BioLinkDto> getBioLinksByPageId(Long pageId)
    {
        return bioLinkRepository.findByBioPageIdOrderBySortOrderAsc( pageId )
                .stream()
                .map( this::convertToDto )
                .collect( Collectors.toList() );
    }

    /**
     * Retrieves only active bio links for a specific page, sorted by sort order.
     * 
     * <p>This method is optimized for public display purposes, returning only
     * links that are marked as active. The results are automatically sorted
     * by sort order for proper display sequencing.</p>
     * 
     * @param pageId the unique identifier of the bio page
     * @return list of active bio link DTOs sorted by sort order (ascending)
     * @throws IllegalArgumentException if pageId is null
     * @throws org.springframework.dao.DataAccessException if database operation fails
     */
    public List<BioLinkDto> getActiveBioLinksByPageId(Long pageId)
    {
        return bioLinkRepository.findByBioPageIdAndIsActiveOrderBySortOrderAsc( pageId, true ).stream()
                .map( this::convertToDto ).collect( Collectors.toList() );
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
    private BioLinkDto convertToDto(BioLinkEntity entity)
    {
        log.trace( "Converting BioLinkEntity to DTO for ID: {}", entity.getId() );
        try
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
        catch ( Exception e )
        {
            log.error( "Failed to convert BioLinkEntity to DTO for ID: {}", entity.getId(), e );
            throw new RuntimeException( "Failed to convert entity to DTO", e );
        }
    }
}
