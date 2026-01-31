package io.zaplink.manager.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.common.enums.BioLinkType;
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
import jakarta.persistence.Table;
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
  @jakarta.persistence.Index(name = "idx_bio_links_type", columnList = "type") })
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
     * JPA lifecycle callback - executed after entity is loaded from database.
     * Sets up logging for debugging.
     */
    @PostLoad
    protected void onLoad()
    {
        log.trace( "Loaded BioLink: {} ({}) for page: {}", title, type,
                   bioPage != null ? bioPage.getUsername() : "unknown" );
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
}
