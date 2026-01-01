package io.zaplink.core.entity;

import java.time.LocalDateTime;

import io.zaplink.core.common.enums.UrlStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data @Entity @Table(name = "url_mapping")
public class UrlMappingEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false)
    private Long          id;
    @Column(name = "short_url_key", nullable = false, unique = true)
    private String        shortUrlKey;
    @Column(name = "original_url", nullable = false, length = 2048)
    private String        originalUrl;
    @Column(name = "short_url", nullable = false)
    private String        shortUrl;
    @Column(name = "user_email")
    private String        userEmail;
    @Column(name = "trace_id")
    private String        traceId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "click_count", nullable = false)
    private Long          clickCount = 0L;
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false)
    private UrlStatusEnum status;
}