package io.zaplink.core.dto.request.qr;

import io.zaplink.core.common.enums.QRBodyShapeEnum;

public record QRBodyConfig(
    QRBodyShapeEnum shape,
    String color,
    String colorDark,
    boolean gradientLinear
) {
    public QRBodyConfig() {
        this(QRBodyShapeEnum.SQUARE, "#000000", null, true);
    }
    
    public QRBodyConfig(QRBodyShapeEnum shape, String color, String colorDark, boolean gradientLinear) {
        this.shape = shape;
        this.color = color;
        this.colorDark = colorDark;
        this.gradientLinear = gradientLinear;
    }
}
