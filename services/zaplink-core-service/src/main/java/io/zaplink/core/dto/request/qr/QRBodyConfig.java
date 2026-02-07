package io.zaplink.core.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.enums.QRBodyShapeEnum;

public record QRBodyConfig( QRBodyShapeEnum shape,
                            String color,
                            @JsonProperty("colorDark") String colorDark,
                            @JsonProperty("gradientLinear") boolean gradientLinear )
{
    public QRBodyConfig()
    {
        this( QRBodyShapeEnum.SQUARE, "#000000", null, true );
    }

    public QRBodyConfig( QRBodyShapeEnum shape,
                         String color,
                         @JsonProperty("color_dark") String colorDark,
                         @JsonProperty("gradient_linear") boolean gradientLinear )
    {
        this.shape = shape;
        this.color = color;
        this.colorDark = colorDark;
        this.gradientLinear = gradientLinear;
    }
}
