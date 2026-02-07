package io.zaplink.core.dto.response.biopage;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.dto.response.biolink.BioLinkResponse;

/**
 * Response DTO for bio page operations in the Core Service (CQRS Write Side).
 * 
 * <p>This record represents the response from write operations on bio pages.
 * It contains the essential information needed to confirm successful operations
 * and provides data for event publishing to update the read model.</p>
 * 
 * <p><strong>Response Characteristics:</strong></p>
 * <ul>
 *   <li>Contains complete bio page information</li>
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
 * @param id unique identifier of the bio page
 * @param username unique username for the bio page
 * @param ownerId identifier of the user who owns this bio page
 * @param title optional title for the bio page
 * @param themeConfig optional theme configuration JSON
 * @param avatarUrl optional avatar URL
 * @param bioText optional bio/description text
 * @param isActive status flag indicating if the page is active
 * @param bioLinks list of associated bio links
 * @param createdAt timestamp when the bio page was created
 * @param updatedAt timestamp when the bio page was last updated
 */
public record BioPageResponse( Long id,
                               String username,
                               @JsonProperty("owner_id") String ownerId,
                               @JsonProperty("theme_config") String themeConfig,
                               @JsonProperty("avatar_url") String avatarUrl,
                               @JsonProperty("bio_text") String bioText,
                               @JsonProperty("created_at") LocalDateTime createdAt,
                               @JsonProperty("updated_at") LocalDateTime updatedAt,
                               @JsonProperty("bio_links") List<BioLinkResponse> bioLinks )
{
    /**
     * Custom toString implementation for structured logging.
     * 
     * <p>This method provides a clean, readable string representation
     * optimized for logging and debugging purposes.</p>
     * 
     * @return formatted string representation of the bio page response
     */
    @Override
    public String toString()
    {
        return String.format( "BioPageResponse{id=%d, username='%s', ownerId='%s', createdAt=%s}", id, username,
                              ownerId, createdAt );
    }
}
