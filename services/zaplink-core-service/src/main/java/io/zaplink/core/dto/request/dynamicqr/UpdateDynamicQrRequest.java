package io.zaplink.core.dto.request.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.dto.request.RedirectRuleDto;
import io.zaplink.core.dto.request.qr.QRConfig;
import jakarta.validation.constraints.NotBlank;

public record UpdateDynamicQrRequest( @JsonProperty("qr_name") @NotBlank(message = ErrorConstant.VALIDATION_QR_NAME_REQUIRED) String qrName,
                                      @JsonProperty("destination_url") @NotBlank(message = ErrorConstant.VALIDATION_DESTINATION_URL_REQUIRED) String destinationUrl,
                                      @JsonProperty("qr_config") QRConfig qrConfig,
                                      @JsonProperty("campaign_id") String campaignId,
                                      // Advanced Features
                                      @JsonProperty("expiration_date") LocalDateTime expirationDate,
                                      String password,
                                      @JsonProperty("scan_limit") Integer scanLimit,
                                      @JsonProperty("allowed_domains") List<String> allowedDomains,
                                      @JsonProperty("track_analytics") Boolean trackAnalytics,
                                      List<RedirectRuleDto> rules )
{
}
