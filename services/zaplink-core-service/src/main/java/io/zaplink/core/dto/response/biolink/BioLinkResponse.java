package io.zaplink.core.dto.response.biolink;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for bio link operations in the Core Service (CQRS Write Side).
 * 
 * <p>This record represents the response from write operations on bio links.
 * It contains the essential information needed to confirm successful operations
 * and provides data for event publishing to update the read model.</p>
 * 
 * <p><strong>Response Characteristics:</strong></p>
 * <ul>
 *   <li>Contains complete bio link information</li>
 *   <li>Includes timestamps for audit trails</li>
 *   <li>Optimized for write operation confirmations</li>
 *   <li>Used for event publishing to read model</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features:</strong></p>
 * <ul>
 *   <li>Record for immutability</li>
 *   <li>Custom toString for logging</li>
 * </ul>
 * 
 * @param id unique identifier of the bio link
 * @param pageId identifier of the bio page this link belongs to
 * @param title title for the bio link
 * @param url URL for the link
 * @param type type of the bio link (LINK, SOCIAL, PRODUCT, EMAIL, PHONE)
 * @param isActive status flag indicating if the link is active
 * @param sortOrder sort order for display positioning
 * @param price price for PRODUCT type links
 * @param currency currency code for PRODUCT type links
 * @param createdAt timestamp when the bio link was created
 * @param updatedAt timestamp when the bio link was last updated
 */
public record BioLinkResponse( Long id,
                               @JsonProperty("page_id") Long pageId,
                               String title,
                               String url,
                               String type,
                               @JsonProperty("is_active") Boolean isActive,
                               @JsonProperty("sort_order") Integer sortOrder,
                               Double price,
                               String currency,
                               @JsonProperty("metadata") String metadata,
                               @JsonProperty("schedule_from") LocalDateTime scheduleFrom,
                               @JsonProperty("schedule_to") LocalDateTime scheduleTo,
                               @JsonProperty("icon_url") String iconUrl,
                               @JsonProperty("thumbnail_url") String thumbnailUrl,
                               @JsonProperty("created_at") LocalDateTime createdAt,
                               @JsonProperty("updated_at") LocalDateTime updatedAt )
{
    /**
     * Custom toString implementation for structured logging.
     * 
     * <p>This method provides a clean, readable string representation
     * optimized for logging and debugging purposes.</p>
     * 
     * @return formatted string representation of the bio link response
     */
    @Override
    public String toString()
    {
        return String
                .format( "BioLinkResponse{id=%d, pageId=%d, title='%s', type='%s', isActive=%s, sortOrder=%d, createdAt=%s}",
                         id, pageId, title, type, isActive, sortOrder, createdAt );
    }
}
