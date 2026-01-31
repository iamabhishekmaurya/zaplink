package io.zaplink.manager.dto.request.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.dto.request.qr.QRConfig;

public record CreateDynamicQrRequest( @JsonProperty("qr_name") String qrName,
                                      @JsonProperty("destination_url") String destinationUrl,
                                      @JsonProperty("qr_config") QRConfig qrConfig,
                                      @JsonProperty("campaign_id") String campaignId,
                                      String password, // Optional
                                      @JsonProperty("scan_limit") Integer scanLimit, // Optional
                                      @JsonProperty("expiration_date") LocalDateTime expirationDate, // Optional
                                      @JsonProperty("allowed_domains") List<String> allowedDomains // Optional
)
{
}
