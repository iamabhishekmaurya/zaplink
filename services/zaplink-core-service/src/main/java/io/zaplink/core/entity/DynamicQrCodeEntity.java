package io.zaplink.core.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Entity @Table(name = "dynamic_qr_codes") @Builder @NoArgsConstructor @AllArgsConstructor
public class DynamicQrCodeEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false)
    private Long          id;
    @Column(name = "qr_key", nullable = false, unique = true)
    private String        qrKey;
    @Column(name = "qr_name")
    private String        qrName;
    @Column(name = "current_destination_url", nullable = false, length = 2048)
    private String        currentDestinationUrl;
    @JdbcTypeCode(SqlTypes.JSON) @Column(name = "qr_config", columnDefinition = "JSONB")
    private String        qrConfig;
    @Column(name = "user_email")
    private String        userEmail;
    @Column(name = "campaign_id")
    private String        campaignId;
    @Column(name = "is_active", nullable = false) @Default
    private Boolean       isActive   = true;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "total_scans", nullable = false) @Default
    private Long          totalScans = 0L;
    @Column(name = "last_scanned")
    private LocalDateTime lastScanned;
}
