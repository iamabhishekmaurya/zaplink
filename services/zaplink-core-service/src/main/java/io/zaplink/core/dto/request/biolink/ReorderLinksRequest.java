package io.zaplink.core.dto.request.biolink;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for reordering bio links in the Core Service (CQRS Write Side).
 * 
 * <p>This record represents the command to reorder bio links within a page
 * with comprehensive validation to ensure data integrity. It follows the
 * CQRS pattern where write operations are separated from read operations.</p>
 * 
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>Page ID is required for ownership validation</li>
 *   <li>At least one link order is required</li>
 *   <li>No duplicate link IDs allowed</li>
 *   <li>Sort orders must be non-negative</li>
 *   <li>Sort orders must form a valid sequence (0, 1, 2, ...)</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features:</strong></p>
 * <ul>
 *   <li>Record for immutability</li>
 *   <li>Compact constructor for validation</li>
 *   <li>Nested records for structure</li>
 * </ul>
 * 
 * @param pageId identifier of the bio page containing the links
 * @param linkOrders list of link order specifications
 */
public record ReorderLinksRequest(
    @JsonProperty("page_id")
    @NotNull(message = "Page ID is required")
    Long pageId,
    
    @NotEmpty(message = "At least one link order is required")
    List<LinkOrder> linkOrders
) {
    
    /**
     * Nested record representing individual link order information.
     * 
     * <p>This record contains the mapping between a link ID and its
     * desired sort order position within the page.</p>
     * 
     * @param linkId unique identifier of the bio link
     * @param sortOrder desired sort order position (0-based)
     */
    public record LinkOrder(
        @JsonProperty("link_id")
        @NotNull(message = "Link ID is required")
        Long linkId,
        
        @JsonProperty("sort_order")
        @NotNull(message = "Sort order is required")
        Integer sortOrder
    ) {
        /**
         * Compact constructor that validates the link order data.
         */
        public LinkOrder {
            if (sortOrder < 0) {
                throw new IllegalArgumentException("Sort order cannot be negative");
            }
        }
    }
    
    /**
     * Compact constructor that validates the reorder request.
     * 
     * <p>This constructor performs comprehensive validation including
     * duplicate prevention and sort order sequence validation.</p>
     */
    public ReorderLinksRequest {
        validateLinkOrders(linkOrders);
    }
    
    /**
     * Validates the list of link orders for business rule compliance.
     * 
     * @param linkOrders the list of link orders to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateLinkOrders(List<LinkOrder> linkOrders) {
        if (linkOrders == null || linkOrders.isEmpty()) {
            throw new IllegalArgumentException("Link orders cannot be null or empty");
        }
        
        // Check for duplicate link IDs
        long uniqueLinkIds = linkOrders.stream()
            .map(LinkOrder::linkId)
            .distinct()
            .count();
        
        if (uniqueLinkIds != linkOrders.size()) {
            throw new IllegalArgumentException("Duplicate link IDs are not allowed");
        }
        
        // Check for duplicate sort orders
        long uniqueSortOrders = linkOrders.stream()
            .map(LinkOrder::sortOrder)
            .distinct()
            .count();
        
        if (uniqueSortOrders != linkOrders.size()) {
            throw new IllegalArgumentException("Duplicate sort orders are not allowed");
        }
        
        // Validate sort order sequence (should be 0, 1, 2, ...)
        for (int i = 0; i < linkOrders.size(); i++) {
            final int index = i; // effectively final for lambda
            boolean found = linkOrders.stream()
                .anyMatch(order -> order.sortOrder().equals(index));
            
            if (!found) {
                throw new IllegalArgumentException("Sort orders must form a valid sequence starting from 0");
            }
        }
    }
}
