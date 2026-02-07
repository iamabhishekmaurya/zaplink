package io.zaplink.manager.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record QRLogoConfig( @JsonProperty("logo_path") @JsonAlias("logoPath") String logoPath,
                            @JsonProperty("size_ratio") @JsonAlias("sizeRatio") double sizeRatio,
                            int padding,
                            @JsonProperty("background_color") @JsonAlias("backgroundColor") String backgroundColor,
                            @JsonProperty("background_enabled") @JsonAlias("backgroundEnabled") boolean backgroundEnabled,
                            @JsonProperty("background_rounded") @JsonAlias("backgroundRounded") boolean backgroundRounded,
                            @JsonProperty("background_corner_radius") @JsonAlias("backgroundCornerRadius") int backgroundCornerRadius,
                            @JsonProperty("remove_quiet_zone") @JsonAlias("removeQuietZone") boolean removeQuietZone,
                            @JsonProperty("margin_size") @JsonAlias("marginSize") int marginSize )
{
    public QRLogoConfig()
    {
        this( null, 0.2, 2, "#FFFFFF", true, true, 20, true, 0 );
    }
}
