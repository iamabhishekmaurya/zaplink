package io.zaplink.core.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.QRBodyConfig;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.request.qr.QREyeConfig;
import io.zaplink.core.service.qr.impl.ZapQrEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController @RequestMapping("${api.base-path}/qr/debug") @RequiredArgsConstructor @Slf4j
public class QRDebugController
{
    private final ZapQrEngine zapQrEngine;
    @GetMapping(value = "/simple", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateSimpleQR( @RequestParam(name = "data", defaultValue = "https://zaplink.io") String data,
                                                    @RequestParam(name = "size", defaultValue = "512") int size )
    {
        try
        {
            log.info( "Generating simple QR with data: {}, size: {}", data, size );
            // Create a basic config
            QRConfig config = new QRConfig();
            config.setData( data );
            config.setSize( size );
            config.setMargin( 1 );
            config.setBackgroundColor( "#FFFFFF" );
            config.setTransparentBackground( false );
            // Set basic body config
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( QRBodyShapeEnum.SQUARE );
            bodyConfig.setColor( "#000000" );
            config.setBody( bodyConfig );
            // Set basic eye config
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( QREyeShapeEnum.SQUARE );
            eyeConfig.setColorOuter( "#000000" );
            eyeConfig.setColorInner( "#000000" );
            config.setEye( eyeConfig );
            log.info( "Config created: {}", config );
            BufferedImage image = zapQrEngine.generate( config );
            // Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "PNG", baos );
            log.info( "QR generated successfully, image size: {}x{}", image.getWidth(), image.getHeight() );
            return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( baos.toByteArray() );
        }
        catch ( Exception e )
        {
            log.error( "Error generating QR", e );
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/styled", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateStyledQR( @RequestParam(name = "data", defaultValue = "https://zaplink.io") String data,
                                                    @RequestParam(name = "size", defaultValue = "512") int size,
                                                    @RequestParam(name = "bodyShape", defaultValue = "ROUNDED") String bodyShape,
                                                    @RequestParam(name = "color", defaultValue = "#0066FF") String color )
    {
        try
        {
            log.info( "Generating styled QR with data: {}, size: {}, shape: {}, color: {}", data, size, bodyShape,
                      color );
            QRConfig config = new QRConfig();
            config.setData( data );
            config.setSize( size );
            config.setMargin( 2 );
            config.setBackgroundColor( "#FFFFFF" );
            // Set body config
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( QRBodyShapeEnum.valueOf( bodyShape.toUpperCase() ) );
            bodyConfig.setColor( color );
            bodyConfig.setColorDark( "#003D99" );
            bodyConfig.setGradientLinear( true );
            config.setBody( bodyConfig );
            // Set eye config
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( QREyeShapeEnum.ROUNDED );
            eyeConfig.setColorOuter( color );
            eyeConfig.setColorInner( "#003D99" );
            config.setEye( eyeConfig );
            log.info( "Styled config created: {}", config );
            BufferedImage image = zapQrEngine.generate( config );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "PNG", baos );
            log.info( "Styled QR generated successfully" );
            return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( baos.toByteArray() );
        }
        catch ( Exception e )
        {
            log.error( "Error generating styled QR", e );
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/test-config")
    public ResponseEntity<QRConfig> getTestConfig()
    {
        QRConfig config = new QRConfig();
        config.setData( "https://zaplink.io" );
        config.setSize( 512 );
        config.setMargin( 2 );
        config.setBackgroundColor( "#FFFFFF" );
        QRBodyConfig bodyConfig = new QRBodyConfig();
        bodyConfig.setShape( QRBodyShapeEnum.ROUNDED );
        bodyConfig.setColor( "#0066FF" );
        bodyConfig.setColorDark( "#003D99" );
        bodyConfig.setGradientLinear( true );
        config.setBody( bodyConfig );
        QREyeConfig eyeConfig = new QREyeConfig();
        eyeConfig.setShape( QREyeShapeEnum.ROUNDED );
        eyeConfig.setColorOuter( "#0066FF" );
        eyeConfig.setColorInner( "#003D99" );
        config.setEye( eyeConfig );
        return ResponseEntity.ok( config );
    }

    @GetMapping("/verify")
    public ResponseEntity<java.util.Map<String, Object>> verifyQR( @RequestParam(name = "data", defaultValue = "TEST") String data )
    {
        try
        {
            log.info( "Verifying QR generation and decode for data: {}", data );
            // Generate QR
            QRConfig config = new QRConfig();
            config.setData( data );
            config.setSize( 512 );
            config.setMargin( 1 );
            config.setBackgroundColor( "#FFFFFF" );
            config.setTransparentBackground( false );
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( QRBodyShapeEnum.SQUARE );
            bodyConfig.setColor( "#000000" );
            config.setBody( bodyConfig );
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( QREyeShapeEnum.SQUARE );
            eyeConfig.setColorOuter( "#000000" );
            eyeConfig.setColorInner( "#000000" );
            config.setEye( eyeConfig );
            BufferedImage image = zapQrEngine.generate( config );
            // Decode it back using ZXing
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource( image );
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap( new com.google.zxing.common.HybridBinarizer( source ) );
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode( bitmap );
            String decodedData = result.getText();
            log.info( "QR decoded successfully! Original: {}, Decoded: {}", data, decodedData );
            return ResponseEntity.ok( java.util.Map.of( "success", true, "originalData", data, "decodedData",
                                                        decodedData, "match", data.equals( decodedData ) ) );
        }
        catch ( Exception e )
        {
            log.error( "QR verification failed", e );
            return ResponseEntity
                    .ok( java.util.Map.of( "success", false, "originalData", data, "error", e.getMessage() ) );
        }
    }

    // Baseline test using ZXing's native renderer (should always work)
    @GetMapping("/verify-native")
    public ResponseEntity<java.util.Map<String, Object>> verifyNativeQR( @RequestParam(name = "data", defaultValue = "TEST") String data )
    {
        try
        {
            log.info( "Verifying QR with NATIVE ZXing renderer for data: {}", data );
            // Generate BitMatrix using ZXing
            java.util.Map<com.google.zxing.EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put( com.google.zxing.EncodeHintType.ERROR_CORRECTION,
                       com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H );
            hints.put( com.google.zxing.EncodeHintType.CHARACTER_SET, "UTF-8" );
            hints.put( com.google.zxing.EncodeHintType.MARGIN, 1 );
            com.google.zxing.common.BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter()
                    .encode( data, com.google.zxing.BarcodeFormat.QR_CODE, 512, 512, hints );
            // Use ZXing's NATIVE renderer (MatrixToImageWriter)
            BufferedImage image = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage( bitMatrix );
            // Decode it back
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource( image );
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap( new com.google.zxing.common.HybridBinarizer( source ) );
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode( bitmap );
            String decodedData = result.getText();
            log.info( "NATIVE QR decoded! Original: {}, Decoded: {}", data, decodedData );
            return ResponseEntity
                    .ok( java.util.Map.of( "success", true, "renderer", "NATIVE_ZXING", "originalData", data,
                                           "decodedData", decodedData, "match", data.equals( decodedData ) ) );
        }
        catch ( Exception e )
        {
            log.error( "NATIVE QR verification failed", e );
            return ResponseEntity.ok( java.util.Map.of( "success", false, "renderer", "NATIVE_ZXING", "originalData",
                                                        data, "error", e.getMessage() ) );
        }
    }

    // Verify styled QR codes with custom shapes
    @GetMapping("/verify-styled")
    public ResponseEntity<java.util.Map<String, Object>> verifyStyledQR( @RequestParam(name = "data", defaultValue = "TEST") String data,
                                                                         @RequestParam(name = "bodyShape", defaultValue = "SQUARE") String bodyShape,
                                                                         @RequestParam(name = "eyeShape", defaultValue = "SQUARE") String eyeShape )
    {
        try
        {
            log.info( "Verifying STYLED QR for data: {}, body: {}, eye: {}", data, bodyShape, eyeShape );
            QRConfig config = new QRConfig();
            config.setData( data );
            config.setSize( 512 );
            config.setMargin( 1 );
            config.setBackgroundColor( "#FFFFFF" );
            config.setTransparentBackground( false );
            QRBodyConfig bodyConfig = new QRBodyConfig();
            bodyConfig.setShape( QRBodyShapeEnum.valueOf( bodyShape.toUpperCase() ) );
            bodyConfig.setColor( "#000000" );
            config.setBody( bodyConfig );
            QREyeConfig eyeConfig = new QREyeConfig();
            eyeConfig.setShape( QREyeShapeEnum.valueOf( eyeShape.toUpperCase() ) );
            eyeConfig.setColorOuter( "#000000" );
            eyeConfig.setColorInner( "#000000" );
            config.setEye( eyeConfig );
            BufferedImage image = zapQrEngine.generate( config );
            // Decode it back
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource( image );
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap( new com.google.zxing.common.HybridBinarizer( source ) );
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode( bitmap );
            String decodedData = result.getText();
            log.info( "STYLED QR decoded! bodyShape: {}, eyeShape: {}, Decoded: {}", bodyShape, eyeShape, decodedData );
            return ResponseEntity.ok( java.util.Map.of( "success", true, "bodyShape", bodyShape, "eyeShape", eyeShape,
                                                        "originalData", data, "decodedData", decodedData, "match",
                                                        data.equals( decodedData ) ) );
        }
        catch ( Exception e )
        {
            log.error( "STYLED QR verification failed for body: {}, eye: {}", bodyShape, eyeShape, e );
            return ResponseEntity.ok( java.util.Map
                    .of( "success", false, "bodyShape", bodyShape, "eyeShape", eyeShape, "originalData", data, "error",
                         e.getMessage() != null ? e.getMessage() : "NotFoundException" ) );
        }
    }
}
