package io.zaplink.manager.dto.request.qr;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.common.enums.QREyeShapeEnum;

public record QREyeConfig( QREyeShapeEnum shape,
                           @JsonProperty("color_outer") @JsonAlias("colorOuter") String colorOuter,
                           @JsonProperty("color_inner") @JsonAlias("colorInner") String colorInner )
{
    public QREyeConfig()
    {
        this( QREyeShapeEnum.SQUARE, "#000000", "#000000" );
    }
}
