package io.zaplink.manager.entity;

import java.time.LocalDateTime;

import io.zaplink.manager.common.enums.RuleDimension;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for storing smart routing rules.
 * Supports both URL Mappings and Dynamic QR Codes.
 */
@Data @Entity @Table(name = "redirect_rules", uniqueConstraints =
{ @UniqueConstraint(columnNames =
        { "url_mapping_id", "dimension", "value" }), @UniqueConstraint(columnNames =
        { "dynamic_qr_code_id", "dimension", "value" }) }) @Builder @NoArgsConstructor @AllArgsConstructor
public class RedirectRuleEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    @Column(name = "url_mapping_id")
    private Long          urlMappingId;
    @Column(name = "dynamic_qr_code_id")
    private Long          dynamicQrCodeId;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private RuleDimension dimension;
    @Column(nullable = false, length = 64)
    private String        value;          // e.g., 'iOS', 'US', 'mobile'
    @Column(name = "destination_url", nullable = false, columnDefinition = "TEXT")
    private String        destinationUrl;
    @Column(nullable = false)
    private Integer       priority;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
