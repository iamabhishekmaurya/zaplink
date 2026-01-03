package io.zaplink.core.dto.request.qr;

import io.zaplink.core.common.enums.QREyeShapeEnum;
import lombok.Data;

@Data
public class QREyeConfig
{
    private QREyeShapeEnum shape      = QREyeShapeEnum.SQUARE;
    private String         colorOuter = "#000000";
    private String         colorInner = "#000000";
}
