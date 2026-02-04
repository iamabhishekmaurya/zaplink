package io.zaplink.core.dto.request.biopage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.QrConstants;

/**
 * Request DTO for creating a new bio page in the Core Service (CQRS Write Side).
 * 
 * <p>This record represents the command to create a new bio page with all
 * necessary validation and business rules. It's designed for the write side
 * of the CQRS pattern, focusing on data integrity and business rule enforcement.</p>
 * 
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>Username must be unique and 3-50 characters</li>
 *   <li>Owner ID is required for ownership validation</li>
 *   <li>Optional fields have reasonable size limits</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features:</strong></p>
 * <ul>
 *   <li>Record for immutability</li>
 *   <li>Compact constructor for validation</li>
 *   <li>Enhanced switch expressions</li>
 * </ul>
 * 
 * @param username unique username for the bio page (3-50 characters)
 * @param ownerId identifier of the user who owns this bio page
 * @param themeConfig optional theme configuration JSON
 * @param avatarUrl optional avatar URL (max 500 characters)
 * @param bioText optional bio/description text (max 500 characters)
 */
public record CreateBioPageRequest(
    @NotBlank(message = ErrorConstant.VALIDATION_USERNAME_REQUIRED)
    @Size(min = 3, max = 50, message = ErrorConstant.VALIDATION_USERNAME_SIZE_RANGE)
    String username,
    
    @JsonProperty("owner_id")
    @NotBlank(message = ErrorConstant.VALIDATION_OWNER_ID_REQUIRED)
    String ownerId,
    
    @JsonProperty("theme_config")
    String themeConfig,
    
    @JsonProperty("avatar_url")
    @Size(max = 500, message = ErrorConstant.VALIDATION_AVATAR_URL_MAX_LENGTH)
    String avatarUrl,
    
    @JsonProperty("bio_text")
    @Size(max = 500, message = ErrorConstant.VALIDATION_BIO_TEXT_MAX_LENGTH)
    String bioText
) {
    
    /**
     * Compact constructor that validates and sanitizes input data.
     * 
     * <p>This constructor performs comprehensive validation including
     * username format validation and input trimming to ensure data integrity.</p>
     */
    public CreateBioPageRequest {
        validateUsername(username);
        username = username != null ? username.trim() : null;
        ownerId = ownerId != null ? ownerId.trim() : null;
        themeConfig = themeConfig != null ? themeConfig.trim() : null;
        avatarUrl = avatarUrl != null ? avatarUrl.trim() : null;
        bioText = bioText != null ? bioText.trim() : null;
    }
    
    /**
     * Validates username format and business rules.
     * 
     * @param username the username to validate
     * @throws IllegalArgumentException if username is invalid
     */
    private void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException(ErrorConstant.ERROR_USERNAME_CANNOT_BE_NULL);
        }
        
        // Enhanced switch expression for validation
        switch (username) {
            case String uname when uname.trim().isEmpty() -> 
                throw new IllegalArgumentException(ErrorConstant.ERROR_USERNAME_CANNOT_BE_EMPTY);
            case String uname when !uname.matches(QrConstants.USERNAME_PATTERN) -> 
                throw new IllegalArgumentException(ErrorConstant.ERROR_USERNAME_FORMAT);
            case String uname when uname.startsWith("-") || uname.startsWith("_") -> 
                throw new IllegalArgumentException(ErrorConstant.ERROR_USERNAME_CANNOT_START_WITH_UNDERSCORE_OR_HYPHEN);
            default -> {
                // Valid username
            }
        }
    }
}
