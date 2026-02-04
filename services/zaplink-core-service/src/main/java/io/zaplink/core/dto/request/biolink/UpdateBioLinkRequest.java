package io.zaplink.core.dto.request.biolink;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.QrConstants;

/**
 * Request DTO for updating an existing bio link in the Core Service (CQRS Write Side).
 * 
 * <p>This record represents the command to update an existing bio link with
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
 * @param title optional title for the bio link
 * @param url optional URL for the link
 * @param type optional type of the bio link
 * @param isActive optional status flag to activate/deactivate the link
 * @param sortOrder optional sort order for display positioning
 * @param price optional price for PRODUCT type links
 * @param currency optional currency code for PRODUCT type links
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateBioLinkRequest(
    String title,
    
    @Size(max = 500, message = ErrorConstant.VALIDATION_URL_MAX_LENGTH)
    String url,
    
    String type,
    
    @JsonProperty("is_active")
    Boolean isActive,
    
    @JsonProperty("sort_order")
    Integer sortOrder,
    
    BigDecimal price,
    
    String currency
) {
    
    /**
     * Compact constructor that validates and sanitizes input data.
     * 
     * <p>This constructor performs validation and trimming for all
     * provided fields to ensure data integrity during updates.</p>
     */
    public UpdateBioLinkRequest {
        title = title != null ? title.trim() : null;
        url = url != null ? url.trim() : null;
        type = type != null ? type.trim().toUpperCase() : null;
        currency = currency != null ? currency.trim().toUpperCase() : null;
        
        // Validate type-specific fields if provided
        if (type != null) {
            validateTypeSpecificFields(type, url, price, currency);
        }
    }
    
    /**
     * Validates type-specific fields using enhanced switch expression.
     * 
     * @param type the link type
     * @param url the URL field
     * @param price the price field
     * @param currency the currency field
     * @throws IllegalArgumentException if validation fails
     */
    private void validateTypeSpecificFields(String type, String url, BigDecimal price, String currency) {
        switch (type) {
            case QrConstants.LINK_TYPE_LINK, QrConstants.LINK_TYPE_SOCIAL -> {
                if (url != null && !url.isEmpty() && !url.matches(QrConstants.URL_PATTERN)) {
                    throw new IllegalArgumentException( ErrorConstant.ERROR_URL_FORMAT );
                }
            }
            case QrConstants.LINK_TYPE_PRODUCT -> {
                if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException( ErrorConstant.ERROR_PRICE_NEGATIVE );
                }
                if (currency != null && currency.length() != 3) {
                    throw new IllegalArgumentException( ErrorConstant.ERROR_CURRENCY_LENGTH );
                }
            }
            case QrConstants.LINK_TYPE_EMAIL -> {
                if (url != null && !url.isEmpty() && !url.matches(QrConstants.EMAIL_PATTERN)) {
                    throw new IllegalArgumentException( ErrorConstant.ERROR_EMAIL_FORMAT );
                }
            }
            case QrConstants.LINK_TYPE_PHONE -> {
                if (url != null && !url.isEmpty() && !url.matches(QrConstants.PHONE_PATTERN)) {
                    throw new IllegalArgumentException( ErrorConstant.ERROR_PHONE_FORMAT );
                }
            }
            default -> throw new IllegalArgumentException( String.format( ErrorConstant.ERROR_UNKNOWN_LINK_TYPE, type ) );
        }
    }
}
