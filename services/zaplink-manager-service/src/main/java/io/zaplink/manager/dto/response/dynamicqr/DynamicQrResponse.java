package io.zaplink.manager.dto.response.dynamicqr;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.dto.request.qr.QRConfig;

public record DynamicQrResponse( @JsonProperty("id") Long id,
                                 @JsonProperty("qr_key") String qrKey,
                                 @JsonProperty("qr_name") String qrName,
                                 @JsonProperty("current_destination_url") String currentDestinationUrl,
                                 @JsonProperty("qr_config") QRConfig qrConfig,
                                 @JsonProperty("redirect_url") String redirectUrl,
                                 @JsonProperty("campaign_id") String campaignId,
                                 @JsonProperty("user_email") String userEmail,
                                 @JsonProperty("is_active") Boolean isActive,
                                 @JsonProperty("total_scans") Long totalScans,
                                 @JsonProperty("created_at") LocalDateTime createdAt,
                                 @JsonProperty("updated_at") LocalDateTime updatedAt,
                                 @JsonProperty("last_scanned") LocalDateTime lastScanned )
{
}
