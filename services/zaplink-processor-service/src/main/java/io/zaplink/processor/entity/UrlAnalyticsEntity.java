package io.zaplink.processor.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data @Entity @Table(name = "url_analytics")
public class UrlAnalyticsEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    @Column(name = "short_url_key", nullable = false)
    private String        shortUrlKey;
    @Column(name = "ip_address")
    private String        ipAddress;
    @Column(name = "user_agent")
    private String        userAgent;
    @Column(name = "referrer")
    private String        referrer;
    @Column(name = "country")
    private String        country;
    @Column(name = "city")
    private String        city;
    @Column(name = "accessed_at")
    private LocalDateTime accessedAt;
}