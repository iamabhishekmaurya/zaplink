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

@Entity @Table(name = "social_accounts") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SocialAccount
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID           id;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private SocialProvider provider;
    @Column(name = "provider_id", nullable = false)
    private String         providerId;
    @Column(name = "access_token", nullable = false, length = 2048)
    private String         accessToken;
    @Column(name = "refresh_token", length = 2048)
    private String         refreshToken;
    @Column(name = "token_expiry")
    private LocalDateTime  tokenExpiry;
    @Column(name = "owner_id", nullable = false)
    private UUID           ownerId;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime  createdAt;
    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime  updatedAt;
}
