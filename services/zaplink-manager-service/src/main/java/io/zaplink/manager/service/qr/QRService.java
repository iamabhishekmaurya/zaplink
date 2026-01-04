package io.zaplink.manager.service.qr;

import io.zaplink.manager.dto.request.qr.QRConfig;

public interface QRService
{
    byte[] generateStyledQrCode( QRConfig config );
}
