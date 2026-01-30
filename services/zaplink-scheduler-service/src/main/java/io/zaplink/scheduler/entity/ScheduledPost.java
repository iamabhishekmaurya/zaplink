package io.zaplink.scheduler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "scheduled_posts") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduledPost
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID          id;
    @Column(columnDefinition = "TEXT")
    private String        caption;
    @ElementCollection @CollectionTable(name = "scheduled_post_media", joinColumns = @JoinColumn(name = "scheduled_post_id")) @Column(name = "media_asset_id")
    private List<UUID>    mediaAssetIds;
    @Column(name = "scheduled_time", nullable = false)
    private Instant       scheduledTime;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PostStatus    status;
    @ElementCollection @CollectionTable(name = "scheduled_post_social_accounts", joinColumns = @JoinColumn(name = "scheduled_post_id")) @Column(name = "social_account_id")
    private List<UUID>    socialAccountIds;
    @Column(name = "owner_id", nullable = false)
    private UUID          ownerId;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    public enum PostStatus {
        DRAFT, SCHEDULED, PUBLISHED, FAILED
    }
}
