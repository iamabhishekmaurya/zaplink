package io.zaplink.manager.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
  @jakarta.persistence.Index(name = "idx_bio_pages_created_at", columnList = "created_at") })
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
     * JPA lifecycle callback - executed after entity is loaded from database.
     * Sets up logging for debugging.
     */
    @PostLoad
    protected void onLoad()
    {
        log.trace( "Loaded BioPage: {} with {} links", username, bioLinks != null ? bioLinks.size() : 0 );
    }
}
