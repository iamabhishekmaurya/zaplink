package io.zaplink.core.dto.response.dynamicqr;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DynamicQrResponse {
    
    private Long id;
    private String qrKey;
    private String qrName;
    private String currentDestinationUrl;
    private String qrImageUrl;
    private String redirectUrl;
    private String campaignId;
    private String userEmail;
    private Boolean isActive;
    private Long totalScans;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastScanned;
}
