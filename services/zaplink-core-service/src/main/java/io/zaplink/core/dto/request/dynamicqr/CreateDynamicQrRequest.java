package io.zaplink.core.dto.request.dynamicqr;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import io.zaplink.core.dto.request.qr.QRConfig;

@Data
public class CreateDynamicQrRequest {
    
    @NotBlank(message = "QR name is required")
    @Size(max = 255, message = "QR name must not exceed 255 characters")
    private String qrName;
    
    @NotBlank(message = "Destination URL is required")
    @Size(max = 2048, message = "Destination URL must not exceed 2048 characters")
    private String destinationUrl;
    
    private String campaignId;
    
    @NotNull(message = "QR configuration is required")
    private QRConfig qrConfig;
}
