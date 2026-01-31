package io.zaplink.manager.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QRConfig( String data,
                        int size,
                        int margin,
                        @JsonProperty("error_correction_level") String errorCorrectionLevel,
                        @JsonProperty("transparent_background") boolean transparentBackground,
                        @JsonProperty("background_color") String backgroundColor,
                        QRBodyConfig body,
                        QREyeConfig eye,
                        QRLogoConfig logo )
{
    public QRConfig()
    {
        this( null, 1024, 0, "H", false, "#FFFFFF", new QRBodyConfig(), new QREyeConfig(), null );
    }
}
