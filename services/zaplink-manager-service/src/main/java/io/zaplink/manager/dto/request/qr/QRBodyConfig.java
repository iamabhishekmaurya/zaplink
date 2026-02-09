package io.zaplink.manager.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.common.enums.QRBodyShapeEnum;

public record QRBodyConfig( QRBodyShapeEnum shape,
                            String color,
                            @JsonProperty("color_dark") @JsonAlias("colorDark") String colorDark,
                            @JsonProperty("gradient_linear") @JsonAlias("gradientLinear") Boolean gradientLinear )
{
    public QRBodyConfig()
    {
        this( QRBodyShapeEnum.SQUARE, "#000000", null, true );
    }
}
