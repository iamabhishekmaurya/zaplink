package io.zaplink.core.qr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.QRBodyConfig;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.request.qr.QREyeConfig;
import io.zaplink.core.service.QRRenderer;
import io.zaplink.core.service.QRService;
import io.zaplink.core.service.ZapQrEngine;

@SpringBootTest
public class AdvancedQRGeneratorTest
{
    private final QRService qrService = new QRService( new ZapQrEngine( new QRRenderer() ) );
    @Test
    public void testCustomStyledQR()
        throws IOException
    {
        // Create a custom configuration using record constructor
        QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.LIQUID, "#7C3AED", "#EC4899", true );
        QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.LEAF, "#7C3AED", "#EC4899" );
        QRConfig config = new QRConfig( "https://zaplink.io",
                                        512,
                                        3,
                                        "H",
                                        false,
                                        "#F0F9FF",
                                        bodyConfig,
                                        eyeConfig,
                                        null );
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
            QRBodyConfig bodyConfig = new QRBodyConfig( shape, "#2563EB", null, true );
            QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.ROUNDED, "#2563EB", "#1E40AF" );
            QRConfig config = new QRConfig( testUrl, testSize, 2, "H", false, "#FFFFFF", bodyConfig, eyeConfig, null );
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
            QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.ROUNDED, "#059669", null, true );
            QREyeConfig eyeConfig = new QREyeConfig( shape, "#059669", "#047857" );
            QRConfig config = new QRConfig( testUrl, testSize, 2, "H", false, "#FFFFFF", bodyConfig, eyeConfig, null );
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
        QRBodyConfig linearBody = new QRBodyConfig( QRBodyShapeEnum.ROUNDED, "#FF6B6B", "#4ECDC4", true );
        QREyeConfig linearEye = new QREyeConfig( QREyeShapeEnum.SQUARE, "#000000", "#000000" );
        QRConfig linearConfig = new QRConfig( testUrl,
                                              testSize,
                                              1,
                                              "H",
                                              false,
                                              "#FFFFFF",
                                              linearBody,
                                              linearEye,
                                              null );
        byte[] linearImage = qrService.generateStyledQrCode( linearConfig );
        BufferedImage linearBuffered = ImageIO.read( new java.io.ByteArrayInputStream( linearImage ) );
        ImageIO.write( linearBuffered, "PNG", new File( "test-linear-gradient.png" ) );
        // Test radial gradient
        QRBodyConfig radialBody = new QRBodyConfig( QRBodyShapeEnum.CIRCLE, "#FF6B6B", "#4ECDC4", false );
        QREyeConfig radialEye = new QREyeConfig( QREyeShapeEnum.SQUARE, "#000000", "#000000" );
        QRConfig radialConfig = new QRConfig( testUrl,
                                              testSize,
                                              1,
                                              "H",
                                              false,
                                              "#FFFFFF",
                                              radialBody,
                                              radialEye,
                                              null );
        byte[] radialImage = qrService.generateStyledQrCode( radialConfig );
        BufferedImage radialBuffered = ImageIO.read( new java.io.ByteArrayInputStream( radialImage ) );
        ImageIO.write( radialBuffered, "PNG", new File( "test-radial-gradient.png" ) );
        System.out.println( "Gradient effects tested successfully!" );
    }
}
