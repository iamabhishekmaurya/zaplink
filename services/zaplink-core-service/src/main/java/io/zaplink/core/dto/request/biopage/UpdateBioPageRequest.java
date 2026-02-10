package io.zaplink.core.dto.request.biopage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.QrConstants;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing bio page in the Core Service (CQRS Write Side).
 * 
 * <p>This record represents the command to update an existing bio page with
 * partial data support. It follows the CQRS pattern where write operations
 * are separated from read operations for optimal performance.</p>
 * 
 * <p><strong>Update Strategy:</strong></p>
 * <ul>
 *   <li>Only non-null fields are updated (partial updates)</li>
 *   <li>Optional fields allow selective updates</li>
 *   <li>Validation ensures data integrity</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features:</strong></p>
 * <ul>
 *   <li>Record for immutability</li>
 *   <li>Compact constructor for validation</li>
 *   <li>Enhanced switch expressions</li>
 * </ul>
 * 
 * @param title optional title for the bio page
 * @param themeConfig optional theme configuration JSON
 * @param avatarUrl optional avatar URL (max 500 characters)
 * @param bioText optional bio/description text (max 500 characters)
 * @param isActive optional status flag to activate/deactivate the page
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateBioPageRequest( String title,
                                    @JsonProperty("theme_config") String themeConfig,
                                    @JsonProperty("avatar_url") @Size(max = 500, message = ErrorConstant.VALIDATION_AVATAR_URL_MAX_LENGTH) String avatarUrl,
                                    @JsonProperty("bio_text") @Size(max = 500, message = ErrorConstant.VALIDATION_BIO_TEXT_MAX_LENGTH) String bioText,
                                    @JsonProperty("is_active") Boolean isActive,
                                    @JsonProperty("cover_url") @Size(max = 500, message = "Cover URL cannot exceed 500 characters") String coverUrl,
                                    @JsonProperty("seo_meta") String seoMeta,
                                    @JsonProperty("is_public") Boolean isPublic )
{
    /**
     * Compact constructor that validates and sanitizes input data.
     * 
     * <p>This constructor performs validation and trimming for all
     * provided fields to ensure data integrity during updates.</p>
     */
    public UpdateBioPageRequest
    {
        title = title != null ? title.trim() : null;
        themeConfig = themeConfig != null ? themeConfig.trim() : null;
        avatarUrl = avatarUrl != null ? avatarUrl.trim() : null;
        bioText = bioText != null ? bioText.trim() : null;
        coverUrl = coverUrl != null ? coverUrl.trim() : null;
        seoMeta = seoMeta != null ? seoMeta.trim() : null;
        // Validate URL if provided
        if ( avatarUrl != null && !avatarUrl.isEmpty() )
        {
            validateUrl( avatarUrl );
        }
        if ( coverUrl != null && !coverUrl.isEmpty() )
        {
            validateUrl( coverUrl );
        }
    }

    /**
     * Validates URL format using enhanced switch expression.
     * 
     * @param url the URL to validate
     * @throws IllegalArgumentException if URL format is invalid
     */
    private void validateUrl( String url )
    {
        switch ( url )
        {
            case String u when u.trim()
                    .isEmpty() -> throw new IllegalArgumentException( ErrorConstant.ERROR_URL_CANNOT_BE_EMPTY );
            case String u when !u
                    .matches( QrConstants.URL_PATTERN ) -> throw new IllegalArgumentException( ErrorConstant.ERROR_URL_TOO_LONG );
            case String u when u
                    .length() > QrConstants.MAX_AVATAR_URL_LENGTH -> throw new IllegalArgumentException( ErrorConstant.ERROR_URL_TOO_LONG );
            default -> {
                // Valid URL
            }
        }
    }
}
