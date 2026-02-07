package io.zaplink.core.dto.request.dynamicqr;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.dto.request.RedirectRuleDto;
import io.zaplink.core.dto.request.qr.QRConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDynamicQrRequest( @JsonProperty("qr_name") @NotBlank(message = ErrorConstant.VALIDATION_QR_NAME_REQUIRED) @Size(max = 255, message = ErrorConstant.VALIDATION_QR_NAME_MAX_LENGTH) String qrName,
                                      @JsonProperty("destination_url") @NotBlank(message = ErrorConstant.VALIDATION_DESTINATION_URL_REQUIRED) @Size(max = 2048, message = ErrorConstant.VALIDATION_DESTINATION_URL_MAX_LENGTH) String destinationUrl,
                                      @JsonProperty("campaign_id") String campaignId,
                                      @JsonProperty("qr_config") @NotNull(message = ErrorConstant.VALIDATION_QR_CONFIG_REQUIRED) QRConfig qrConfig,
                                      // Advanced Features
                                      @JsonProperty("expiration_days") Integer expirationDays,
                                      String password,
                                      @JsonProperty("scan_limit") Integer scanLimit,
                                      @JsonProperty("allowed_domains") String allowedDomains, // Comma separated or JSON
                                      @JsonProperty("track_analytics") Boolean trackAnalytics,
                                      List<RedirectRuleDto> rules )
{
    /**
     * Compact constructor for default values.
     */
    public CreateDynamicQrRequest
    {
        trackAnalytics = trackAnalytics != null ? trackAnalytics : true;
    }
}
