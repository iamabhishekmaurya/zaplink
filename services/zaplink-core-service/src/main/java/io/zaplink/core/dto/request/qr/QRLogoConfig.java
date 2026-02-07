package io.zaplink.core.dto.request.qr;

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

    public QRLogoConfig( @JsonProperty("logo_path") String logoPath,
                         @JsonProperty("size_ratio") double sizeRatio,
                         int padding,
                         @JsonProperty("background_color") String backgroundColor,
                         @JsonProperty("background_enabled") boolean backgroundEnabled,
                         @JsonProperty("background_rounded") boolean backgroundRounded,
                         @JsonProperty("background_corner_radius") int backgroundCornerRadius,
                         @JsonProperty("remove_quiet_zone") boolean removeQuietZone,
                         @JsonProperty("margin_size") int marginSize )
    {
        this.logoPath = logoPath;
        this.sizeRatio = sizeRatio;
        this.padding = padding;
        this.backgroundColor = backgroundColor;
        this.backgroundEnabled = backgroundEnabled;
        this.backgroundRounded = backgroundRounded;
        this.backgroundCornerRadius = backgroundCornerRadius;
        this.removeQuietZone = removeQuietZone;
        this.marginSize = marginSize;
    }
}
