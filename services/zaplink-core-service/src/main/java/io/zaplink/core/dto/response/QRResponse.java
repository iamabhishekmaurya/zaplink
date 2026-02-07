package io.zaplink.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QRResponse( @JsonProperty("qr_code") String qrCode,
                          @JsonProperty("qr_code_url") String qrCodeUrl,
                          @JsonProperty("qr_code_file") String qrCodeFile,
                          @JsonProperty("qr_code_file_url") String qrCodeFileUrl )
{
}
