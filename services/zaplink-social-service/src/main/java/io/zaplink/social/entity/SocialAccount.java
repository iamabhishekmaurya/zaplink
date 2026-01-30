package io.zaplink.social.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.zaplink.social.common.enums.SocialProvider;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a user's connected social media account.
 * <p>
 * Stores OAuth2 credentials and metadata required to publish content on behalf of the user.
 * </p>
 */
@Entity @Table(name = "social_accounts") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SocialAccount
{
    /**
     * Unique identifier for this connection record.
     */
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID           id;
    /**
     * The social platform (e.g., INSTAGRAM, FACEBOOK).
     */
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private SocialProvider provider;
    /**
     * The unique ID of the user on the provider's platform (e.g., Instagram User ID).
     */
    @Column(name = "provider_id", nullable = false)
    private String         providerId;
    /**
     * The OAuth2 access token used to authenticate API requests.
     */
    @Column(name = "access_token", nullable = false, length = 2048)
    private String         accessToken;
    /**
     * The OAuth2 refresh token used to obtain new access tokens when they expire.
     */
    @Column(name = "refresh_token", length = 2048)
    private String         refreshToken;
    /**
     * The expiration timestamp of the current access token.
     */
    @Column(name = "token_expiry")
    private LocalDateTime  tokenExpiry;
    /**
     * The ID of the Zaplink user who owns this account.
     */
    @Column(name = "owner_id", nullable = false)
    private UUID           ownerId;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime  createdAt;
    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime  updatedAt;
}
