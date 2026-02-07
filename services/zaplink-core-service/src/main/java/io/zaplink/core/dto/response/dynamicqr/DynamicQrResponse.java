package io.zaplink.core.dto.response.dynamicqr;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DynamicQrResponse( Long id,
                                 @JsonProperty("qr_key") String qrKey,
                                 @JsonProperty("qr_name") String qrName,
                                 @JsonProperty("current_destination_url") String currentDestinationUrl,
                                 @JsonProperty("qr_image_url") String qrImageUrl,
                                 @JsonProperty("redirect_url") String redirectUrl,
                                 @JsonProperty("campaign_id") String campaignId,
                                 @JsonProperty("user_email") String userEmail,
                                 @JsonProperty("is_active") Boolean isActive,
                                 @JsonProperty("total_scans") Long totalScans,
                                 @JsonProperty("created_at") LocalDateTime createdAt,
                                 @JsonProperty("updated_at") LocalDateTime updatedAt,
                                 @JsonProperty("last_scanned") LocalDateTime lastScanned,
                                 // Advanced Fields
                                 @JsonProperty("qr_config") Object qrConfig,
                                 @JsonProperty("allowed_domains") Object allowedDomains,
                                 String password,
                                 @JsonProperty("scan_limit") Integer scanLimit,
                                 @JsonProperty("expiration_date") LocalDateTime expirationDate,
                                 @JsonProperty("track_analytics") Boolean trackAnalytics )
{
}
