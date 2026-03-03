package io.zaplink.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "media_items")
public class MediaItem
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID               id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "folder_id")
    private Folder             folder;
    @Column(name = "owner_id", nullable = false)
    private String             ownerId;
    @Column(nullable = false)
    private String             name;
    @Column(nullable = false)
    private String             type;
    @Column(nullable = false)
    private String             url;
    @Column(name = "size_bytes", nullable = false)
    private Long               size;
    @JdbcTypeCode(SqlTypes.JSON) @Column(columnDefinition = "jsonb")
    private List<String>       tags;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime      createdAt;
    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime      updatedAt;
    @Builder.Default @Column(name = "is_deleted", nullable = false)
    private boolean            isDeleted  = false;
    @Builder.Default @Column(name = "is_favorite", nullable = false)
    private boolean            isFavorite = false;
    @Builder.Default @Column(nullable = false)
    private Integer            version    = 1;
    @JdbcTypeCode(SqlTypes.JSON) @Column(name = "shared_with", columnDefinition = "jsonb")
    private List<Collaborator> sharedWith;
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Collaborator
    {
        private String userId;
        private String role;  // "viewer", "editor"
    }
}
