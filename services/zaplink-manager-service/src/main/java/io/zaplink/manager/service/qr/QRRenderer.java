package io.zaplink.manager.service.qr;

import com.google.zxing.common.BitMatrix;

import io.zaplink.manager.dto.request.qr.QRConfig;

import java.awt.image.BufferedImage;

public interface QRRenderer
{
    BufferedImage render( BitMatrix matrix, QRConfig config );
}
