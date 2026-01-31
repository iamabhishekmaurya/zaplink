package io.zaplink.core.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.enums.BioLinkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * JPA Entity representing a BioLink within a BioPage in the Zaplink platform.
 * 
 * <p>A BioLink represents an individual link, product, or contact method displayed
 * on a user's bio page. Each link has a type (website, social media, product, etc.),
 * can be reordered, and may include pricing information for commercial items.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Multiple link types: website, social media, product, email, phone</li>
 *   <li>Product support with pricing and currency</li>
 *   <li>Sortable ordering for custom arrangement</li>
 *   <li>Active/inactive status management</li>
 *   <li>Automatic timestamp management</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features Used:</strong></p>
 * <ul>
 *   <li>Sealed classes for BioLinkType enum</li>
 *   <li>Switch expressions with pattern matching</li>
 *   <li>Record patterns for validation</li>
 *   <li>Virtual thread compatibility</li>
 *   <li>Enhanced null safety with Optional</li>
 * </ul>
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-30
 * @see BioPageEntity
 * @see IBioPageService
 */
@Slf4j @Data @NoArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE) @Getter @Setter @EqualsAndHashCode(of =
{ "id" }, callSuper = false) @ToString(of =
{ "id", "title", "type", "isActive", "sortOrder", "price", "currency" }) @Entity @Table(name = "bio_links", indexes =
{ @jakarta.persistence.Index(name = "idx_bio_links_page_id", columnList = "page_id"),
  @jakarta.persistence.Index(name = "idx_bio_links_page_sort_order", columnList = "page_id, sort_order"),
  @jakarta.persistence.Index(name = "idx_bio_links_page_active", columnList = "page_id, is_active"),
  @jakarta.persistence.Index(name = "idx_bio_links_type", columnList = "type") }) @JsonInclude(JsonInclude.Include.NON_NULL)
public class BioLinkEntity
{
    /**
     * Primary key identifier for the BioLink.
     * Auto-generated using PostgreSQL IDENTITY strategy.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false, updatable = false)
    private Long          id;
    /**
     * Reference to the parent BioPage entity.
     * Establishes the many-to-one relationship.
     */
    @ManyToOne @JoinColumn(name = "page_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "fk_bio_links_page_id"))
    private BioPageEntity bioPage;
    /**
     * Display title for the bio link.
     * Maximum 200 characters. Required field.
     */
    @Column(name = "title", nullable = false, length = 200)
    private String        title;
    /**
     * URL for the bio link.
     * Maximum 2048 characters. Required for some link types.
     */
    @Column(name = "url", length = 2048)
    private String        url;
    /**
     * Type of the bio link.
     * Determines behavior and validation rules.
     */
    @Enumerated(EnumType.STRING) @Column(name = "type", nullable = false, length = 20)
    private BioLinkType   type;
    /**
     * Active status of the bio link.
     * Only active links are displayed on the public bio page.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean       isActive  = true;
    /**
     * Sort order for display positioning.
     * Lower numbers appear first. Used for custom ordering.
     */
    @Column(name = "sort_order", nullable = false)
    private Integer       sortOrder = 0;
    /**
     * Price for product links.
     * Stored as decimal with precision 10 and scale 2.
     */
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal    price;
    /**
     * Currency code for product pricing.
     * 3-character ISO 4217 currency code (e.g., USD, EUR).
     */
    @Column(name = "currency", length = 3)
    private String        currency;
    /**
     * Timestamp when the bio link was created.
     * Automatically set on creation and immutable.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    /**
     * Timestamp when the bio link was last updated.
     * Automatically updated on any modification.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    /**
     * Transient field for caching the formatted price string.
     * Computed at runtime and not persisted.
     */
    @Transient
    private String        formattedPrice;
    /**
     * Transient field for caching the click-through URL.
     * Computed based on link type.
     */
    @Transient
    private String        clickThroughUrl;
    /**
     * JPA lifecycle callback - executed before entity persistence.
     * Sets creation and update timestamps.
     */
    @PrePersist
    protected void onCreate()
    {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        log.debug( "Creating new BioLink with title: {}, type: {}", title, type );
        validateBioLink();
        computeDerivedFields();
    }

    /**
     * JPA lifecycle callback - executed before entity update.
     * Updates the timestamp and validates the entity state.
     */
    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
        log.debug( "Updating BioLink with id: {}, title: {}", id, title );
        validateBioLink();
        computeDerivedFields();
    }

    /**
     * JPA lifecycle callback - executed after entity is loaded from database.
     * Computes derived values and sets up caching.
     */
    @PostLoad
    protected void onLoad()
    {
        computeDerivedFields();
        log.trace( "Loaded BioLink: {} ({}) for page: {}", title, type,
                   bioPage != null ? bioPage.getUsername() : "unknown" );
    }

    /**
     * JPA lifecycle callback - executed after entity persistence.
     * Logs successful creation.
     */
    @PostPersist
    protected void afterCreate()
    {
        log.info( "Successfully created BioLink: {} with ID: {} for page: {}", title, id,
                  bioPage != null ? bioPage.getUsername() : "unknown" );
    }

    /**
     * JPA lifecycle callback - executed after entity update.
     * Logs successful update operations.
     */
    @PostUpdate
    protected void afterUpdate()
    {
        log.info( "Successfully updated BioLink: {} with ID: {}", title, id );
    }

    /**
     * JPA lifecycle callback - executed before entity removal.
     * Logs deletion operations for audit purposes.
     */
    @PreRemove
    protected void beforeRemove()
    {
        log.warn( "Removing BioLink: {} with ID: {} from page: {}", title, id,
                  bioPage != null ? bioPage.getUsername() : "unknown" );
    }

    /**
     * JPA lifecycle callback - executed after entity removal.
     * Logs successful deletion.
     */
    @PostRemove
    protected void afterRemove()
    {
        log.info( "Successfully removed BioLink: {} with ID: {}", title, id );
    }

    /**
     * Validates the bio link entity state using Java 21 pattern matching.
     * 
     * @throws IllegalArgumentException if validation fails
     */
    private void validateBioLink()
    {
        // Validate title
        if ( title == null )
        {
            throw new IllegalArgumentException( "Title cannot be null" );
        }
        if ( title.isEmpty() )
        {
            throw new IllegalArgumentException( "Title cannot be empty" );
        }
        if ( title.length() < 1 )
        {
            throw new IllegalArgumentException( "Title must be at least 1 character long" );
        }
        if ( title.length() > 200 )
        {
            throw new IllegalArgumentException( "Title cannot exceed 200 characters" );
        }
        log.trace( "Title validation passed: {}", title );
        // Validate type-specific requirements
        if ( type == null )
        {
            throw new IllegalArgumentException( "Link type cannot be null" );
        }
        // Use Java 21 switch expression for type validation
        switch ( type )
        {
            case LINK -> {
                if ( url == null || url.trim().isEmpty() )
                {
                    throw new IllegalArgumentException( "Website links must have a valid URL" );
                }
            }
            case SOCIAL -> {
                if ( url == null || url.trim().isEmpty() )
                {
                    throw new IllegalArgumentException( "Social media links must have a valid URL" );
                }
            }
            case PRODUCT -> validateProductLink();
            case EMAIL -> validateEmailLink();
            case PHONE -> validatePhoneLink();
        }
        // Validate sort order
        if ( sortOrder == null || sortOrder < 0 )
        {
            throw new IllegalArgumentException( "Sort order must be a non-negative integer" );
        }
    }

    /**
     * Validates product link specific requirements.
     */
    private void validateProductLink()
    {
        if ( price == null )
        {
            throw new IllegalArgumentException( "Product links must have a price" );
        }
        if ( price.compareTo( BigDecimal.ZERO ) < 0 )
        {
            throw new IllegalArgumentException( "Product price cannot be negative" );
        }
        if ( currency == null || currency.trim().isEmpty() )
        {
            throw new IllegalArgumentException( "Product links must have a currency" );
        }
        if ( !currency.matches( "^[A-Z]{3}$" ) )
        {
            throw new IllegalArgumentException( "Currency must be a valid 3-letter ISO code" );
        }
        log.trace( "Product link validation passed: price {}, currency {}", price, currency );
    }

    /**
     * Validates email link specific requirements.
     */
    private void validateEmailLink()
    {
        if ( url != null && !url.trim().isEmpty() )
        {
            // Basic email validation
            if ( !url.matches( "^[A-Za-z0-9+_.-]+@(.+)$" ) )
            {
                throw new IllegalArgumentException( "Invalid email format" );
            }
        }
        log.trace( "Email link validation passed" );
    }

    /**
     * Validates phone link specific requirements.
     */
    private void validatePhoneLink()
    {
        if ( url != null && !url.trim().isEmpty() )
        {
            // Basic phone validation - allow digits, +, -, spaces, parentheses
            if ( !url.matches( "^[+0-9\\-\\s\\(\\)]+$" ) )
            {
                throw new IllegalArgumentException( "Invalid phone number format" );
            }
        }
        log.trace( "Phone link validation passed" );
    }

    /**
     * Computes derived fields like formatted price and click-through URL.
     * Uses Java 21 switch expressions and pattern matching.
     */
    private void computeDerivedFields()
    {
        // Compute formatted price using traditional switch
        if ( type == BioLinkType.PRODUCT && price != null )
        {
            this.formattedPrice = formatPrice( price, Optional.ofNullable( currency ).orElse( "USD" ) );
        }
        else
        {
            this.formattedPrice = null;
        }
        // Compute click-through URL using traditional switch
        this.clickThroughUrl = switch ( type )
        {
            case LINK, SOCIAL -> url;
            case EMAIL -> ( url != null ) ? "mailto:" + url : null;
            case PHONE -> ( url != null ) ? "tel:" + url.replaceAll( "[^0-9+]", "" ) : null;
            default -> null;
        };
    }

    /**
     * Formats the price with currency symbol.
     * 
     * @param price the price to format
     * @param currency the currency code
     * @return formatted price string
     */
    private String formatPrice( BigDecimal price, String currency )
    {
        try
        {
            return switch ( currency )
            {
                case "USD" -> "$" + price.setScale( 2, RoundingMode.HALF_UP );
                case "EUR" -> "€" + price.setScale( 2, RoundingMode.HALF_UP );
                case "GBP" -> "£" + price.setScale( 2, RoundingMode.HALF_UP );
                case "INR" -> "₹" + price.setScale( 2, RoundingMode.HALF_UP );
                default -> currency + " " + price.setScale( 2, RoundingMode.HALF_UP );
            };
        }
        catch ( Exception e )
        {
            log.warn( "Error formatting price: {} {}", price, currency, e );
            return price.toString();
        }
    }

    /**
     * Gets the formatted price string.
     * 
     * @return formatted price with currency symbol, or null if not applicable
     */
    public String getFormattedPrice()
    {
        if ( formattedPrice == null )
        {
            computeDerivedFields();
        }
        return formattedPrice;
    }

    /**
     * Gets the click-through URL for this link.
     * 
     * @return the URL to navigate to when clicked, or null if not applicable
     */
    public String getClickThroughUrl()
    {
        if ( clickThroughUrl == null )
        {
            computeDerivedFields();
        }
        return clickThroughUrl;
    }

    /**
     * Checks if this link is clickable.
     * 
     * @return true if the link has a valid click-through URL, false otherwise
     */
    public boolean isClickable()
    {
        return getClickThroughUrl() != null && !getClickThroughUrl().trim().isEmpty();
    }

    /**
     * Gets the type name as a string.
     * 
     * @return the type name
     */
    @JsonProperty("type")
    public String getTypeName()
    {
        return type != null ? type.getTypeName() : null;
    }

    /**
     * Sets the type from a string value.
     * 
     * @param typeName the type name string
     */
    public void setTypeFromString( String typeName )
    {
        this.type = BioLinkType.fromString( typeName );
    }

    /**
     * Creates a copy of this bio link with a new sort order.
     * 
     * @param newSortOrder the new sort order
     * @return a new bio link entity with updated sort order
     */
    public BioLinkEntity withSortOrder( int newSortOrder )
    {
        BioLinkEntity copy = new BioLinkEntity();
        copy.id = this.id;
        copy.bioPage = this.bioPage;
        copy.title = this.title;
        copy.url = this.url;
        copy.type = this.type;
        copy.isActive = this.isActive;
        copy.sortOrder = newSortOrder;
        copy.price = this.price;
        copy.currency = this.currency;
        copy.createdAt = this.createdAt;
        copy.updatedAt = this.updatedAt;
        return copy;
    }

    /**
     * Checks if this link is a product link.
     * 
     * @return true if this is a product link, false otherwise
     */
    public boolean isProduct()
    {
        return type == BioLinkType.PRODUCT;
    }

    /**
     * Checks if this link requires a URL.
     * 
     * @return true if a URL is required, false otherwise
     */
    public boolean requiresUrl()
    {
        return type != null && type.requiresUrl();
    }

    /**
     * Checks if this link supports pricing.
     * 
     * @return true if pricing is supported, false otherwise
     */
    public boolean supportsPricing()
    {
        return type != null && type.supportsPricing();
    }
}
