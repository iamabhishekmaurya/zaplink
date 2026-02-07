package io.zaplink.core.dto.request.qr;

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
        this( null, 1024, 1, "H", false, "#FFFFFF", new QRBodyConfig(), new QREyeConfig(), null );
    }

    public QRConfig( String data,
                     int size,
                     int margin,
                     @JsonProperty("error_correction_level") String errorCorrectionLevel,
                     @JsonProperty("transparent_background") boolean transparentBackground,
                     @JsonProperty("background_color") String backgroundColor,
                     QRBodyConfig body,
                     QREyeConfig eye,
                     QRLogoConfig logo )
    {
        this.data = data;
        this.size = size;
        this.margin = margin;
        this.errorCorrectionLevel = errorCorrectionLevel;
        this.transparentBackground = transparentBackground;
        this.backgroundColor = backgroundColor;
        this.body = body;
        this.eye = eye;
        this.logo = logo;
    }
}
