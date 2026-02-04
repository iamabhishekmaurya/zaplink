package io.zaplink.core.dto.request.biolink;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record representing a request to create a new bio link.
 * 
 * <p>This immutable record uses Java 21 features for concise data carrier
 * implementation with comprehensive validation annotations.</p>
 * 
 * @param pageId the ID of the bio page this link belongs to
 * @param title the display title of the link
 * @param url the target URL (optional for some link types)
 * @param type the type of link (LINK, SOCIAL, PRODUCT, EMAIL, PHONE)
 * @param isActive whether the link is currently active
 * @param sortOrder the display order among other links
 * @param price the price for product links (optional)
 * @param currency the currency code for product links (optional)
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-30
 * @see io.zaplink.manager.entity.BioLinkEntity.BioLinkType
 */
public record CreateBioLinkRequest( @JsonProperty("page_id") @NotNull(message = ErrorConstant.VALIDATION_PAGE_ID_REQUIRED) Long pageId,
                                    @NotBlank(message = ErrorConstant.VALIDATION_TITLE_REQUIRED) @Size(max = 200, message = ErrorConstant.VALIDATION_TITLE_MAX_LENGTH) String title,
                                    @Size(max = 2048, message = ErrorConstant.VALIDATION_URL_MAX_LENGTH) String url,
                                    @NotBlank(message = ErrorConstant.VALIDATION_TYPE_REQUIRED) String type,
                                    @JsonProperty("is_active") Boolean isActive,
                                    @JsonProperty("sort_order") @Min(value = 0, message = ErrorConstant.VALIDATION_MIN_VALUE) Integer sortOrder,
                                    Double price,
                                    String currency )
{
    /**
     * Compact constructor for validation and default values.
     * 
     * <p>This constructor runs before the primary constructor and allows
     * for validation logic and default value assignment using Java 21 features.</p>
     */
    public CreateBioLinkRequest
    {
        // Set default values using Java 21 pattern matching
        isActive = isActive != null ? isActive : true;
        sortOrder = sortOrder != null ? sortOrder : 0;
        // Validate type-specific requirements
        validateTypeSpecificFields( type, url, price, currency );
    }

    /**
     * Validates fields based on link type using Java 21 enhanced switch expressions.
     * 
     * @param type the link type
     * @param url the URL field
     * @param price the price field
     * @param currency the currency field
     * @throws IllegalArgumentException if validation fails
     */
    private void validateTypeSpecificFields( String type, String url, Double price, String currency )
    {
        if ( type == null || type.trim().isEmpty() )
        {
            return; // Will be caught by @NotBlank annotation
        }
        // Use Java 21 enhanced switch expression for type-specific validation
        switch ( type.toUpperCase() )
        {
            case "LINK", "SOCIAL" -> {
                if ( url == null || url.trim().isEmpty() )
                {
                    throw new IllegalArgumentException( "URL is required for " + type + " links" );
                }
            }
            case "PRODUCT" -> {
                if ( price == null || currency == null )
                {
                    throw new IllegalArgumentException( "Product links must have both price and currency" );
                }
                if ( price <= 0 )
                {
                    throw new IllegalArgumentException( "Product price must be positive" );
                }
                if ( !currency.matches( "^[A-Z]{3}$" ) )
                {
                    throw new IllegalArgumentException( "Currency must be a valid 3-letter ISO code" );
                }
            }
            case "EMAIL" -> {
                if ( url != null && !url.trim().isEmpty() )
                {
                    if ( !url.matches( "^[A-Za-z0-9+_.-]+@(.+)$" ) )
                    {
                        throw new IllegalArgumentException( "Invalid email format" );
                    }
                }
            }
            case "PHONE" -> {
                if ( url != null && !url.trim().isEmpty() )
                {
                    if ( !url.matches( "^[+0-9\\-\\s\\(\\)]+$" ) )
                    {
                        throw new IllegalArgumentException( "Invalid phone number format" );
                    }
                }
            }
            default -> throw new IllegalArgumentException( "Unknown link type: " + type );
        }
    }
}
