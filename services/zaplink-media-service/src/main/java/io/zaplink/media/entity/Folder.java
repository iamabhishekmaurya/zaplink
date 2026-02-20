package io.zaplink.media.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "folders")
public class Folder
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID          id;
    @Column(nullable = false)
    private String        name;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_id") @JsonIgnoreProperties(
    { "hibernateLazyInitializer", "handler" })
    private Folder        parent;
    @Column(name = "owner_id", nullable = false)
    private String        ownerId;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_deleted", nullable = false)
    private boolean       isDeleted  = false;
    @Column(name = "is_favorite", nullable = false)
    private boolean       isFavorite = false;
}
