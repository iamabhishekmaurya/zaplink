package io.zaplink.core.dto.request.dynamicqr;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.request.RedirectRuleDto;
import java.util.List;

public record CreateDynamicQrRequest(
    @NotBlank(message = "QR name is required") @Size(max = 255, message = "QR name must not exceed 255 characters")
    String qrName,
    
    @NotBlank(message = "Destination URL is required") @Size(max = 2048, message = "Destination URL must not exceed 2048 characters")
    String destinationUrl,
    
    String campaignId,
    
    @NotNull(message = "QR configuration is required")
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
