package io.zaplink.core.qr;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.*;
import io.zaplink.core.service.qr.impl.QRRendererImpl;
import io.zaplink.core.service.qr.impl.ZapQrEngine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class QRGenerationTest
{
    private final ZapQrEngine qrEngine = new ZapQrEngine( new QRRendererImpl() );
    @Test
    public void testBasicQRGeneration()
        throws IOException
    {
        QRConfig config = new QRConfig();
        config.setData( "https://zaplink.io" );
        config.setSize( 512 );
        config.setMargin( 1 );
        config.setBackgroundColor( "#FFFFFF" );
        QRBodyConfig bodyConfig = new QRBodyConfig();
        bodyConfig.setShape( QRBodyShapeEnum.SQUARE );
        bodyConfig.setColor( "#000000" );
        config.setBody( bodyConfig );
        QREyeConfig eyeConfig = new QREyeConfig();
        eyeConfig.setShape( QREyeShapeEnum.SQUARE );
        eyeConfig.setColorOuter( "#000000" );
        eyeConfig.setColorInner( "#000000" );
        config.setEye( eyeConfig );
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
        QRConfig config = new QRConfig();
        config.setData( "https://zaplink.io" );
        config.setSize( 512 );
        config.setMargin( 2 );
        config.setBackgroundColor( "#FFFFFF" );
        QRBodyConfig bodyConfig = new QRBodyConfig();
        bodyConfig.setShape( QRBodyShapeEnum.ROUNDED );
        bodyConfig.setColor( "#0066FF" );
        config.setBody( bodyConfig );
        QREyeConfig eyeConfig = new QREyeConfig();
        eyeConfig.setShape( QREyeShapeEnum.ROUNDED );
        eyeConfig.setColorOuter( "#0066FF" );
        eyeConfig.setColorInner( "#003D99" );
        config.setEye( eyeConfig );
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
        QRConfig config = new QRConfig();
        config.setData( "https://zaplink.io" );
        config.setSize( 512 );
        config.setMargin( 2 );
        config.setBackgroundColor( "#FFFFFF" );
        QRBodyConfig bodyConfig = new QRBodyConfig();
        bodyConfig.setShape( QRBodyShapeEnum.CIRCLE );
        bodyConfig.setColor( "#FF6B6B" );
        bodyConfig.setColorDark( "#4ECDC4" );
        bodyConfig.setGradientLinear( true );
        config.setBody( bodyConfig );
        QREyeConfig eyeConfig = new QREyeConfig();
        eyeConfig.setShape( QREyeShapeEnum.CIRCLE );
        eyeConfig.setColorOuter( "#FF6B6B" );
        eyeConfig.setColorInner( "#4ECDC4" );
        config.setEye( eyeConfig );
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
            QRConfig config = new QRConfig();
            config.setData( testData );
            config.setSize( 256 ); // Smaller size for testing
            config.setMargin( 1 );
            config.setBackgroundColor( "#FFFFFF" );
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( shape );
            bodyConfig.setColor( "#2563EB" );
            config.setBody( bodyConfig );
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( QREyeShapeEnum.ROUNDED );
            eyeConfig.setColorOuter( "#2563EB" );
            eyeConfig.setColorInner( "#1E40AF" );
            config.setEye( eyeConfig );
            System.out.println( "Generating QR with shape: " + shape );
            BufferedImage image = qrEngine.generate( config );
            // Save each shape
            ImageIO.write( image, "PNG", new File( "test-shape-" + shape.name().toLowerCase() + ".png" ) );
            System.out.println( "Shape " + shape + " generated successfully!" );
        }
    }
}
