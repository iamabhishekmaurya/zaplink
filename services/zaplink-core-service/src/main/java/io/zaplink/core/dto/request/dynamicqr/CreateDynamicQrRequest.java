package io.zaplink.core.dto.request.dynamicqr;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.request.RedirectRuleDto;
import io.zaplink.core.common.constants.ErrorConstant;

public record CreateDynamicQrRequest(
    @NotBlank(message = ErrorConstant.VALIDATION_QR_NAME_REQUIRED) @Size(max = 255, message = ErrorConstant.VALIDATION_QR_NAME_MAX_LENGTH)
    String qrName,
    
    @NotBlank(message = ErrorConstant.VALIDATION_DESTINATION_URL_REQUIRED) @Size(max = 2048, message = ErrorConstant.VALIDATION_DESTINATION_URL_MAX_LENGTH)
    String destinationUrl,
    
    String campaignId,
    
    @NotNull(message = ErrorConstant.VALIDATION_QR_CONFIG_REQUIRED)
    QRConfig qrConfig,
    
    // Advanced Features
    Integer expirationDays,
    String password,
    Integer scanLimit,
    String allowedDomains,       // Comma separated or JSON
    Boolean trackAnalytics,
    List<RedirectRuleDto> rules
) {
    /**
     * Compact constructor for default values.
     */
    public CreateDynamicQrRequest {
        trackAnalytics = trackAnalytics != null ? trackAnalytics : true;
    }
}
