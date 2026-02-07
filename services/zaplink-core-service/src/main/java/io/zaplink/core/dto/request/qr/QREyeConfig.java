package io.zaplink.core.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.enums.QREyeShapeEnum;

public record QREyeConfig( QREyeShapeEnum shape,
                           @JsonProperty("color_outer") String colorOuter,
                           @JsonProperty("color_inner") String colorInner )
{
    public QREyeConfig()
    {
        this( QREyeShapeEnum.SQUARE, "#000000", "#000000" );
    }

    public QREyeConfig( QREyeShapeEnum shape,
                        @JsonProperty("color_outer") String colorOuter,
                        @JsonProperty("color_inner") String colorInner )
    {
        this.shape = shape;
        this.colorOuter = colorOuter;
        this.colorInner = colorInner;
    }
}
