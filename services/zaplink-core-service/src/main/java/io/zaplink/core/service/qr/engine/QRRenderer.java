package io.zaplink.core.service.qr.engine;

import com.google.zxing.common.BitMatrix;

import io.zaplink.core.dto.request.qr.QRConfig;

import java.awt.image.BufferedImage;

public interface QRRenderer
{
    BufferedImage render( BitMatrix matrix, QRConfig config );
}
