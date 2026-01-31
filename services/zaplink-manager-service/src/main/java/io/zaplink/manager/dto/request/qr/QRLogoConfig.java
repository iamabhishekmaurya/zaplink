package io.zaplink.manager.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QRLogoConfig( @JsonProperty("logo_path") String logoPath,
                            @JsonProperty("size_ratio") double sizeRatio,
                            int padding,
                            @JsonProperty("background_color") String backgroundColor,
                            @JsonProperty("background_enabled") boolean backgroundEnabled,
                            @JsonProperty("background_rounded") boolean backgroundRounded,
                            @JsonProperty("background_corner_radius") int backgroundCornerRadius,
                            @JsonProperty("remove_quiet_zone") boolean removeQuietZone,
                            @JsonProperty("margin_size") int marginSize )
{
    public QRLogoConfig()
    {
        this( null, 0.2, 2, "#FFFFFF", true, true, 20, true, 0 );
    }
}
