package io.zaplink.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "qr_scan_analytics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrScanAnalyticsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "qr_key", nullable = false)
    private String qrKey;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "referrer")
    private String referrer;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "browser")
    private String browser;
    
    @Column(name = "scanned_at", nullable = false)
    private LocalDateTime scannedAt;
}
