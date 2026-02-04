package io.zaplink.core.dto.request.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;

import io.zaplink.core.dto.request.RedirectRuleDto;
import io.zaplink.core.dto.request.qr.QRConfig;
import jakarta.validation.constraints.NotBlank;

import io.zaplink.core.common.constants.ErrorConstant;

public record UpdateDynamicQrRequest( @NotBlank(message = ErrorConstant.VALIDATION_QR_NAME_REQUIRED) String qrName,
                                      @NotBlank(message = ErrorConstant.VALIDATION_DESTINATION_URL_REQUIRED) String destinationUrl,
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
