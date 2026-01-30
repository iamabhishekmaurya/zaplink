package io.zaplink.scheduler.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.zaplink.scheduler.common.constants.DbIdentifiers;
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

@Entity @Table(name = DbIdentifiers.TABLE_SCHEDULED_POSTS) @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduledPost
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID          id;
    @Column(columnDefinition = "TEXT")
    private String        caption;
    @ElementCollection @CollectionTable(name = DbIdentifiers.TABLE_SCHEDULED_POST_MEDIA, joinColumns = @JoinColumn(name = DbIdentifiers.JOIN_COL_SCHEDULED_POST_ID)) @Column(name = DbIdentifiers.COL_MEDIA_ASSET_ID)
    private List<UUID>    mediaAssetIds;
    @JsonAlias("scheduledAt") // Accept "scheduledAt" from frontend
    @Column(name = DbIdentifiers.COL_SCHEDULED_TIME, nullable = false)
    private Instant       scheduledTime;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PostStatus    status;
    @ElementCollection @CollectionTable(name = DbIdentifiers.TABLE_SCHEDULED_POST_ACCOUNTS, joinColumns = @JoinColumn(name = DbIdentifiers.JOIN_COL_SCHEDULED_POST_ID)) @Column(name = DbIdentifiers.COL_SOCIAL_ACCOUNT_ID)
    private List<UUID>    socialAccountIds;
    @Column(name = DbIdentifiers.COL_OWNER_ID, nullable = false)
    private String        ownerId;
    @CreationTimestamp @Column(name = DbIdentifiers.COL_CREATED_AT, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = DbIdentifiers.COL_UPDATED_AT)
    private LocalDateTime updatedAt;
    public enum PostStatus {
        DRAFT, SCHEDULED, PUBLISHED, FAILED
    }
}
