package io.zaplink.core.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class QRResponse
{
    private String qrCode;
    private String qrCodeUrl;
    private String qrCodeFile;
    private String qrCodeFileUrl;
}
