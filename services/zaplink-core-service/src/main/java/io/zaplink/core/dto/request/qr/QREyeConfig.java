package io.zaplink.core.dto.request.qr;

import io.zaplink.core.common.enums.QREyeShapeEnum;

public record QREyeConfig(
    QREyeShapeEnum shape,
    String colorOuter,
    String colorInner
) {
    public QREyeConfig() {
        this(QREyeShapeEnum.SQUARE, "#000000", "#000000");
    }
    
    public QREyeConfig(QREyeShapeEnum shape, String colorOuter, String colorInner) {
        this.shape = shape;
        this.colorOuter = colorOuter;
        this.colorInner = colorInner;
    }
}
