package io.zaplink.core.dto.request.qr;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import lombok.Data;

@Data
public class QRBodyConfig
{
    private QRBodyShapeEnum shape          = QRBodyShapeEnum.SQUARE;
    private String          color          = "#000000";
    private String          colorDark      = null;                  // For gradient end
    private boolean         gradientLinear = true;                  // true = linear, false = radial
}
