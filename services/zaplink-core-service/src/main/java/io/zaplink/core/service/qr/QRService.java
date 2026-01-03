package io.zaplink.core.service.qr;

import io.zaplink.core.dto.request.qr.QRConfig;

public interface QRService {
    byte[] generateStyledQrCode( QRConfig config );
}
