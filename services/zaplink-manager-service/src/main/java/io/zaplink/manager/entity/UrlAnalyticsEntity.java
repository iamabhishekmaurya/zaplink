package io.zaplink.manager.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data @Entity(name = "url_analytics")
public class UrlAnalyticsEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "short_url_key", nullable = false)
    private String     shortUrlKey;
    @Column(name = "ip_address")
    private String     ipAddress;
    @Column(name = "user_agent")
    private String     userAgent;
    @Column(name = "referrer")
    private String     referrer;
    @Column(name = "country")
    private String     country;
    @Column(name = "city")
    private String     city;
    @Column(name = "accessed_at")
    private Timestamp  accessedAt;
}