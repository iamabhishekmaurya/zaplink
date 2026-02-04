package io.zaplink.core.dto.request.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;

import io.zaplink.core.dto.request.RedirectRuleDto;
import io.zaplink.core.dto.request.qr.QRConfig;
import jakarta.validation.constraints.NotBlank;

public record UpdateDynamicQrRequest( @NotBlank(message = "QR Name is required") String qrName,
                                      @NotBlank(message = "Destination URL is required") String destinationUrl,
                                      QRConfig qrConfig,
                                      String campaignId,
                                      // Advanced Features
                                      LocalDateTime expirationDate,
                                      String password,
                                      Integer scanLimit,
                                      List<String> allowedDomains,
                                      Boolean trackAnalytics,
                                      List<RedirectRuleDto> rules )
{
}
