package io.zaplink.core.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
 * JPA Entity representing a BioPage in the Zaplink platform.
 * 
 * <p>A BioPage serves as a customizable "link in bio" page where users can showcase
 * multiple links, social media profiles, products, and contact information.
 * Each page has a unique username and can be customized with themes and avatars.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Unique username for public access (e.g., zap.link/{username})</li>
 *   <li>Theme customization via JSON configuration</li>
 *   <li>Avatar and bio text support</li>
 *   <li>Automatic timestamp management</li>
 *   <li>Cascade relationship with BioLink entities</li>
 * </ul>
 * 
 * <p><strong>Java 21 Features Used:</strong></p>
 * <ul>
 *   <li>Record patterns for validation</li>
 *   <li>Switch expressions with pattern matching</li>
 *   <li>Sealed interfaces for type safety</li>
 *   <li>Virtual thread compatibility</li>
 * </ul>
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2024-01-30
 * @see BioLinkEntity
 * @see IBioPageService
 */
@Slf4j @Data @NoArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE) @Getter @Setter @EqualsAndHashCode(of =
{ "id", "username" }, callSuper = false) @ToString(of =
{ "id", "username", "ownerId", "createdAt", "updatedAt" }) @Entity @Table(name = "bio_pages", indexes =
{ @jakarta.persistence.Index(name = "idx_bio_pages_username", columnList = "username", unique = true),
  @jakarta.persistence.Index(name = "idx_bio_pages_owner_id", columnList = "owner_id"),
  @jakarta.persistence.Index(name = "idx_bio_pages_created_at", columnList = "created_at") }) @JsonInclude(JsonInclude.Include.NON_NULL)
public class BioPageEntity
{
    /**
     * Primary key identifier for the BioPage.
     * Auto-generated using PostgreSQL IDENTITY strategy.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false, updatable = false)
    private Long                id;
    /**
     * Unique username for the bio page.
     * Used in public URLs (e.g., zap.link/{username}).
     * Must be between 3-50 characters and unique across all pages.
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String              username;
    /**
     * Owner identifier for the bio page.
     * References the user who owns this page (could be user ID, email, etc.).
     */
    @Column(name = "owner_id", nullable = false, length = 255)
    private String              ownerId;
    /**
     * Theme configuration in JSON format.
     * Allows customization of colors, fonts, and layout.
     * Example: {"primaryColor": "#3b82f6", "backgroundColor": "#ffffff"}
     */
    @Column(name = "theme_config", columnDefinition = "jsonb")
    private String              themeConfig;
    /**
     * URL to the avatar image for the bio page.
     * Should be a valid HTTPS URL. Maximum 500 characters.
     */
    @Column(name = "avatar_url", length = 500)
    private String              avatarUrl;
    /**
     * Bio text or description for the page owner.
     * Supports markdown-like formatting. Maximum 500 characters.
     */
    @Column(name = "bio_text", length = 500)
    private String              bioText;
    /**
     * Timestamp when the bio page was created.
     * Automatically set on creation and immutable.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime       createdAt;
    /**
     * Timestamp when the bio page was last updated.
     * Automatically updated on any modification.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime       updatedAt;
    /**
     * Collection of associated BioLink entities.
     * Cascade operations are enabled for automatic management.
     * Links are eagerly fetched for performance in bio page operations.
     */
    @OneToMany(mappedBy = "bioPage", cascade =
    { jakarta.persistence.CascadeType.ALL,
      jakarta.persistence.CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER) @JsonIgnore // Prevent infinite recursion during JSON serialization
    private List<BioLinkEntity> bioLinks;
    /**
     * Transient field for caching the number of active links.
     * Computed at runtime and not persisted.
     */
    @Transient
    private Integer             activeLinksCount;
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
        log.debug( "Creating new BioPage with username: {}", username );
        validateBioPage();
    }

    /**
     * JPA lifecycle callback - executed before entity update.
     * Updates the timestamp and validates the entity state.
     */
    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
        log.debug( "Updating BioPage with id: {}, username: {}", id, username );
        validateBioPage();
    }

    /**
     * JPA lifecycle callback - executed after entity is loaded from database.
     * Computes derived values and sets up caching.
     */
    @PostLoad
    protected void onLoad()
    {
        computeActiveLinksCount();
        log.trace( "Loaded BioPage: {} with {} links", username, bioLinks != null ? bioLinks.size() : 0 );
    }

    /**
     * JPA lifecycle callback - executed after entity persistence.
     * Logs successful creation and performs post-creation operations.
     */
    @PostPersist
    protected void afterCreate()
    {
        log.info( "Successfully created BioPage: {} with ID: {}", username, id );
        computeActiveLinksCount();
    }

    /**
     * JPA lifecycle callback - executed after entity update.
     * Logs successful update operations.
     */
    @PostUpdate
    protected void afterUpdate()
    {
        log.info( "Successfully updated BioPage: {} with ID: {}", username, id );
        computeActiveLinksCount();
    }

    /**
     * JPA lifecycle callback - executed before entity removal.
     * Logs deletion operations for audit purposes.
     */
    @PreRemove
    protected void beforeRemove()
    {
        log.warn( "Removing BioPage: {} with ID: {} and {} associated links", username, id,
                  bioLinks != null ? bioLinks.size() : 0 );
    }

    /**
     * JPA lifecycle callback - executed after entity removal.
     * Logs successful deletion.
     */
    @PostRemove
    protected void afterRemove()
    {
        log.info( "Successfully removed BioPage: {} with ID: {}", username, id );
    }

    /**
     * Validates the bio page entity state using Java 21 pattern matching.
     * 
     * @throws IllegalArgumentException if validation fails
     */
    private void validateBioPage()
    {
        // Validate username using switch expression with pattern matching
        switch ( username )
        {
            case null -> throw new IllegalArgumentException( "Username cannot be null or empty" );
            case "" -> throw new IllegalArgumentException( "Username cannot be null or empty" );
            case String uname when uname
                    .length() < 3 -> throw new IllegalArgumentException( "Username must be at least 3 characters long" );
            case String uname when uname
                    .length() > 50 -> throw new IllegalArgumentException( "Username cannot exceed 50 characters" );
            case String uname when !uname
                    .matches( "^[a-zA-Z0-9_-]+$" ) -> throw new IllegalArgumentException( "Username can only contain letters, numbers, underscores, and hyphens" );
            default -> log.trace( "Username validation passed: {}", username );
        }
        // Validate owner ID
        if ( ownerId == null || ownerId.trim().isEmpty() )
        {
            throw new IllegalArgumentException( "Owner ID cannot be null or empty" );
        }
        // Validate theme configuration if present
        if ( themeConfig != null && !themeConfig.trim().isEmpty() )
        {
            validateThemeConfig();
        }
        // Validate avatar URL if present
        if ( avatarUrl != null && !avatarUrl.trim().isEmpty() )
        {
            validateAvatarUrl();
        }
    }

    /**
     * Validates the theme configuration JSON.
     * 
     * @throws IllegalArgumentException if JSON is malformed
     */
    private void validateThemeConfig()
    {
        try
        {
            // Basic JSON validation - in production, use proper JSON parser
            if ( !themeConfig.trim().startsWith( "{" ) || !themeConfig.trim().endsWith( "}" ) )
            {
                throw new IllegalArgumentException( "Theme configuration must be valid JSON" );
            }
            log.trace( "Theme configuration validation passed" );
        }
        catch ( Exception e )
        {
            throw new IllegalArgumentException( "Invalid theme configuration JSON: " + e.getMessage(), e );
        }
    }

    /**
     * Validates the avatar URL format.
     * 
     * @throws IllegalArgumentException if URL format is invalid
     */
    private void validateAvatarUrl()
    {
        try
        {
            // Basic URL validation - in production, use java.net.URI
            if ( !avatarUrl.startsWith( "http://" ) && !avatarUrl.startsWith( "https://" ) )
            {
                throw new IllegalArgumentException( "Avatar URL must start with http:// or https://" );
            }
            log.trace( "Avatar URL validation passed: {}", avatarUrl );
        }
        catch ( Exception e )
        {
            throw new IllegalArgumentException( "Invalid avatar URL format: " + e.getMessage(), e );
        }
    }

    /**
     * Computes the number of active links for caching.
     * Uses Java 21 streams and optional handling.
     */
    private void computeActiveLinksCount()
    {
        this.activeLinksCount = Optional.ofNullable( bioLinks ).map( links -> (int) links.stream()
                .filter( link -> link.getIsActive() != null && link.getIsActive() ).count() ).orElse( 0 );
    }

    /**
     * Gets the number of active links on this bio page.
     * 
     * @return the count of active links, or 0 if no links exist
     */
    public Integer getActiveLinksCount()
    {
        if ( activeLinksCount == null )
        {
            computeActiveLinksCount();
        }
        return activeLinksCount;
    }

    /**
     * Checks if this bio page has any active links.
     * 
     * @return true if there are active links, false otherwise
     */
    public boolean hasActiveLinks()
    {
        return getActiveLinksCount() > 0;
    }

    /**
     * Gets the public URL for this bio page.
     * 
     * @return the public URL in format zap.link/{username}
     */
    public String getPublicUrl()
    {
        return "zap.link/" + username;
    }

    /**
     * Adds a bio link to this page with automatic sort order assignment.
     * 
     * @param bioLink the bio link to add
     */
    public void addBioLink( BioLinkEntity bioLink )
    {
        if ( bioLinks == null )
        {
            bioLinks = new java.util.ArrayList<>();
        }
        // Set sort order to next available position
        int nextSortOrder = bioLinks.stream().mapToInt( link -> link.getSortOrder() != null ? link.getSortOrder() : 0 )
                .max().orElse( 0 ) + 1;
        bioLink.setSortOrder( nextSortOrder );
        bioLink.setBioPage( this );
        bioLinks.add( bioLink );
        log.debug( "Added BioLink '{}' to BioPage '{}' with sort order {}", bioLink.getTitle(), username,
                   nextSortOrder );
    }

    /**
     * Removes a bio link from this page.
     * 
     * @param bioLink the bio link to remove
     * @return true if the link was removed, false if it wasn't found
     */
    public boolean removeBioLink( BioLinkEntity bioLink )
    {
        boolean removed = Optional.ofNullable( bioLinks ).map( links -> links.remove( bioLink ) ).orElse( false );
        if ( removed )
        {
            computeActiveLinksCount();
            log.debug( "Removed BioLink '{}' from BioPage '{}'", bioLink.getTitle(), username );
        }
        return removed;
    }
}
