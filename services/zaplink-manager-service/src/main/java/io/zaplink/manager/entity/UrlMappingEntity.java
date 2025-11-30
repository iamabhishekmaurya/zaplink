package io.zaplink.manager.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data @Entity(name = "url_mapping")
public class UrlMappingEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false)
    private BigInteger id;
    @Column(name = "short_url_key", nullable = false, unique = true)
    private String     shortUrlKey;
    @Column(name = "original_url", nullable = false)
    private String     originalUrl;
    @Column(name = "short_url", nullable = false)
    private String     shortUrl;
    @Column(name = "trace_id")
    private String     traceId;
    @Column(name = "created_at", nullable = false)
    private Timestamp  createdAt;
    @Column(name = "expires_at")
    private Timestamp  expiresAt;
    @Column(name = "click_count")
    private Long       clickCount;
    @Column(name = "status", nullable = false)
    private String     status;
}