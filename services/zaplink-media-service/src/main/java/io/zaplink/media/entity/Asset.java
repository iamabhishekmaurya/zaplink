package io.zaplink.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "assets")
public class Asset
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID          id;
    @Column(name = "owner_id", nullable = false)
    private UUID          ownerId;
    @Column(nullable = false)
    private String        url;
    @Column(nullable = false)
    private String        filename;
    @Column(name = "mime_type")
    private String        mimeType;
    @Column(name = "size_bytes")
    private Long          sizeBytes;
    private Integer       width;
    private Integer       height;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "folder_id")
    private Folder        folder;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
