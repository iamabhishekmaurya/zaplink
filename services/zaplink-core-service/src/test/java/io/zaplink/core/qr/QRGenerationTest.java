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
import io.zaplink.core.service.ZapQrEngine;

@SpringBootTest
public class QRGenerationTest
{
    private final ZapQrEngine qrEngine = new ZapQrEngine( new QRRenderer() );
    @Test
    public void testBasicQRGeneration()
        throws IOException
    {
        QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.SQUARE, "#000000", null, true );
        QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.SQUARE, "#000000", "#000000" );
        QRConfig config = new QRConfig( "https://zaplink.io",
                                        512,
                                        1,
                                        "H",
                                        false,
                                        "#FFFFFF",
                                        bodyConfig,
                                        eyeConfig,
                                        null );
        System.out.println( "Generating basic QR..." );
        BufferedImage image = qrEngine.generate( config );
        // Save to file
        ImageIO.write( image, "PNG", new File( "test-basic-qr.png" ) );
        System.out.println( "Basic QR generated successfully! Size: " + image.getWidth() + "x" + image.getHeight() );
    }

    @Test
    public void testRoundedQRGeneration()
        throws IOException
    {
        QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.ROUNDED, "#0066FF", null, true );
        QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.ROUNDED, "#0066FF", "#003D99" );
        QRConfig config = new QRConfig( "https://zaplink.io",
                                        512,
                                        2,
                                        "H",
                                        false,
                                        "#FFFFFF",
                                        bodyConfig,
                                        eyeConfig,
                                        null );
        System.out.println( "Generating rounded QR..." );
        BufferedImage image = qrEngine.generate( config );
        // Save to file
        ImageIO.write( image, "PNG", new File( "test-rounded-qr.png" ) );
        System.out.println( "Rounded QR generated successfully! Size: " + image.getWidth() + "x" + image.getHeight() );
    }

    @Test
    public void testGradientQRGeneration()
        throws IOException
    {
        QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.CIRCLE, "#FF6B6B", "#4ECDC4", true );
        QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.CIRCLE, "#FF6B6B", "#4ECDC4" );
        QRConfig config = new QRConfig( "https://zaplink.io",
                                        512,
                                        2,
                                        "H",
                                        false,
                                        "#FFFFFF",
                                        bodyConfig,
                                        eyeConfig,
                                        null );
        System.out.println( "Generating gradient QR..." );
        BufferedImage image = qrEngine.generate( config );
        // Save to file
        ImageIO.write( image, "PNG", new File( "test-gradient-qr.png" ) );
        System.out.println( "Gradient QR generated successfully! Size: " + image.getWidth() + "x" + image.getHeight() );
    }

    @Test
    public void testAllShapes()
        throws IOException
    {
        String testData = "https://zaplink.io/test";
        for ( QRBodyShapeEnum shape : QRBodyShapeEnum.values() )
        {
            QRBodyConfig bodyConfig = new QRBodyConfig( shape, "#2563EB", null, true );
            QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.ROUNDED, "#2563EB", "#1E40AF" );
            QRConfig config = new QRConfig( testData, 256, 1, "H", false, "#FFFFFF", bodyConfig, eyeConfig, null );
            System.out.println( "Generating QR with shape: " + shape );
            BufferedImage image = qrEngine.generate( config );
            // Save each shape
            ImageIO.write( image, "PNG", new File( "test-shape-" + shape.name().toLowerCase() + ".png" ) );
            System.out.println( "Shape " + shape + " generated successfully!" );
        }
    }
}
