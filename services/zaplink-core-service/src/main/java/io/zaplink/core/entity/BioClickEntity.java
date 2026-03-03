package io.zaplink.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Table(name = "bio_clicks") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BioClickEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    @ManyToOne @JoinColumn(name = "link_id", nullable = false)
    private BioLinkEntity link;
    @ManyToOne @JoinColumn(name = "page_id", nullable = false)
    private BioPageEntity page;
    @Column(name = "referrer", length = 2048)
    private String        referrer;
    @Column(name = "user_agent", length = 1000)
    private String        userAgent;
    @Column(name = "ip_hash", length = 64)
    private String        ipHash;
    @Column(name = "country_code", length = 2)
    private String        countryCode;
    @Column(name = "device_type", length = 20)
    private String        deviceType;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
