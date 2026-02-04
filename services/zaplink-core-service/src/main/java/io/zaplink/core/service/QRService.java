package io.zaplink.core.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.dto.request.qr.QRConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j @Primary
public class QRService
{
    private final ZapQrEngine zapQrEngine;
    // Main method for generating styled QR codes with custom configurations
    public byte[] generateStyledQrCode( QRConfig config )
    {
        try
        {
            BufferedImage image = zapQrEngine.generate( config );
            return imageToBytes( image );
        }
        catch ( Exception e )
        {
            log.error( "Failed to generate styled QR code", e );
            throw new RuntimeException( ErrorConstant.ERROR_STYLED_QR_CODE_GENERATION_FAILED, e );
        }
    }

    private byte[] imageToBytes( BufferedImage image )
        throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( image, "PNG", baos );
        return baos.toByteArray();
    }
}
