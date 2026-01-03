package io.zaplink.core.qr;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.*;
import io.zaplink.core.service.qr.impl.QRRendererImpl;
import io.zaplink.core.service.qr.impl.QRServiceImpl;
import io.zaplink.core.service.qr.impl.ZapQrEngine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class AdvancedQRGeneratorTest
{
    private final QRServiceImpl qrService = new QRServiceImpl( new ZapQrEngine( new QRRendererImpl() ) );

    @Test
    public void testCustomStyledQR()
        throws IOException
    {
        // Create a custom configuration
        QRConfig config = new QRConfig();
        config.setData( "https://zaplink.io" );
        config.setSize( 512 );
        config.setMargin( 3 );
        config.setBackgroundColor( "#F0F9FF" );
        // Custom body configuration
        QRBodyConfig bodyConfig = new QRBodyConfig();
        bodyConfig.setShape( QRBodyShapeEnum.LIQUID );
        bodyConfig.setColor( "#7C3AED" );
        bodyConfig.setColorDark( "#EC4899" );
        bodyConfig.setGradientLinear( true );
        config.setBody( bodyConfig );
        // Custom eye configuration
        QREyeConfig eyeConfig = new QREyeConfig();
        eyeConfig.setShape( QREyeShapeEnum.LEAF );
        eyeConfig.setColorOuter( "#7C3AED" );
        eyeConfig.setColorInner( "#EC4899" );
        config.setEye( eyeConfig );
        byte[] qrImage = qrService.generateStyledQrCode( config );
        // Save to file for visual inspection
        BufferedImage image = ImageIO.read( new java.io.ByteArrayInputStream( qrImage ) );
        ImageIO.write( image, "PNG", new File( "test-custom-style.png" ) );
        System.out.println( "Custom style QR generated successfully!" );
    }

    @Test
    public void testAllBodyShapes()
        throws IOException
    {
        String testUrl = "https://zaplink.io";
        int testSize = 256;
        for ( QRBodyShapeEnum shape : QRBodyShapeEnum.values() )
        {
            QRConfig config = new QRConfig();
            config.setData( testUrl );
            config.setSize( testSize );
            config.setMargin( 2 );
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( shape );
            bodyConfig.setColor( "#2563EB" );
            config.setBody( bodyConfig );
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( QREyeShapeEnum.ROUNDED );
            eyeConfig.setColorOuter( "#2563EB" );
            eyeConfig.setColorInner( "#1E40AF" );
            config.setEye( eyeConfig );
            byte[] qrImage = qrService.generateStyledQrCode( config );
            // Save each shape
            BufferedImage image = ImageIO.read( new java.io.ByteArrayInputStream( qrImage ) );
            ImageIO.write( image, "PNG", new File( "test-shape-" + shape.name().toLowerCase() + ".png" ) );
            System.out.println( "Generated QR with body shape: " + shape );
        }
    }

    @Test
    public void testAllEyeShapes()
        throws IOException
    {
        String testUrl = "https://zaplink.io";
        int testSize = 256;
        for ( QREyeShapeEnum shape : QREyeShapeEnum.values() )
        {
            QRConfig config = new QRConfig();
            config.setData( testUrl );
            config.setSize( testSize );
            config.setMargin( 2 );
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( QRBodyShapeEnum.ROUNDED );
            bodyConfig.setColor( "#059669" );
            config.setBody( bodyConfig );
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( shape );
            eyeConfig.setColorOuter( "#059669" );
            eyeConfig.setColorInner( "#047857" );
            config.setEye( eyeConfig );
            byte[] qrImage = qrService.generateStyledQrCode( config );
            // Save each eye shape
            BufferedImage image = ImageIO.read( new java.io.ByteArrayInputStream( qrImage ) );
            ImageIO.write( image, "PNG", new File( "test-eye-" + shape.name().toLowerCase() + ".png" ) );
            System.out.println( "Generated QR with eye shape: " + shape );
        }
    }

    @Test
    public void testGradientEffects()
        throws IOException
    {
        String testUrl = "https://zaplink.io";
        int testSize = 512;
        // Test linear gradient
        QRConfig linearConfig = new QRConfig();
        linearConfig.setData( testUrl );
        linearConfig.setSize( testSize );
        QRBodyConfig linearBody = new QRBodyConfig();
        linearBody.setShape( QRBodyShapeEnum.ROUNDED );
        linearBody.setColor( "#FF6B6B" );
        linearBody.setColorDark( "#4ECDC4" );
        linearBody.setGradientLinear( true );
        linearConfig.setBody( linearBody );
        byte[] linearImage = qrService.generateStyledQrCode( linearConfig );
        BufferedImage linearBuffered = ImageIO.read( new java.io.ByteArrayInputStream( linearImage ) );
        ImageIO.write( linearBuffered, "PNG", new File( "test-linear-gradient.png" ) );
        // Test radial gradient
        QRConfig radialConfig = new QRConfig();
        radialConfig.setData( testUrl );
        radialConfig.setSize( testSize );
        QRBodyConfig radialBody = new QRBodyConfig();
        radialBody.setShape( QRBodyShapeEnum.CIRCLE );
        radialBody.setColor( "#FF6B6B" );
        radialBody.setColorDark( "#4ECDC4" );
        radialBody.setGradientLinear( false ); // Radial
        radialConfig.setBody( radialBody );
        byte[] radialImage = qrService.generateStyledQrCode( radialConfig );
        BufferedImage radialBuffered = ImageIO.read( new java.io.ByteArrayInputStream( radialImage ) );
        ImageIO.write( radialBuffered, "PNG", new File( "test-radial-gradient.png" ) );
        System.out.println( "Gradient effects tested successfully!" );
    }
}
