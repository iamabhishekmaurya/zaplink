package io.zaplink.core.service.qr.impl;

import io.zaplink.core.dto.request.qr.*;
import io.zaplink.core.service.qr.QRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service @RequiredArgsConstructor @Slf4j @Primary
public class QRServiceImpl implements QRService
{
    private final ZapQrEngine zapQrEngine;
    
    // Main method for generating styled QR codes with custom configurations
    @Override
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
            throw new RuntimeException( "Styled QR code generation failed", e );
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
