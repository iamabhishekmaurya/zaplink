package io.zaplink.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "activity_logs")
public class ActivityLog
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID          id;
    @Column(name = "user_id", nullable = false)
    private String        userId;
    @Column(nullable = false)
    private String        action;     // e.g., "UPLOAD", "DELETE", "SHARE", "RENAME"
    @Column(name = "target_id", nullable = false)
    private UUID          targetId;
    @Column(name = "target_type", nullable = false)
    private String        targetType; // e.g., "FOLDER", "MEDIA_ITEM"
    @CreationTimestamp @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
