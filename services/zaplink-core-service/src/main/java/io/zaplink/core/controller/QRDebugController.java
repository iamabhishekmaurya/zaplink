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
import io.zaplink.core.common.constants.MessageConstants;
import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.QRBodyConfig;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.request.qr.QREyeConfig;
import io.zaplink.core.service.ZapQrEngine;
import io.zaplink.core.common.constants.ControllerConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController @RequestMapping(ControllerConstants.QR_DEBUG_BASE_PATH) @RequiredArgsConstructor @Slf4j
public class QRDebugController
{
    private final ZapQrEngine zapQrEngine;
    @GetMapping(value = ControllerConstants.QR_DEBUG_SIMPLE_PATH, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateSimpleQR( @RequestParam(name = ControllerConstants.PARAM_DATA, defaultValue = ControllerConstants.DEFAULT_QR_DATA) String data,
                                                    @RequestParam(name = ControllerConstants.PARAM_SIZE, defaultValue = "512") int size )
    {
        try
        {
            log.info( LogConstants.CONTROLLER_GENERATING_SIMPLE_QR, data, size );
            // Create a basic config using record constructor
            QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.SQUARE,
                                                        ControllerConstants.DEFAULT_FOREGROUND_COLOR,
                                                        null,
                                                        true );
            QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.SQUARE,
                                                     ControllerConstants.DEFAULT_FOREGROUND_COLOR,
                                                     ControllerConstants.DEFAULT_FOREGROUND_COLOR );
            QRConfig config = new QRConfig( data,
                                            size,
                                            ControllerConstants.DEFAULT_MARGIN,
                                            ControllerConstants.DEFAULT_ERROR_CORRECTION,
                                            ControllerConstants.DEFAULT_TRANSPARENT,
                                            ControllerConstants.DEFAULT_WHITE_COLOR,
                                            bodyConfig,
                                            eyeConfig,
                                            null );
            log.info( LogConstants.CONTROLLER_CONFIG_CREATED, config );
            BufferedImage image = zapQrEngine.generate( config );
            // Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "PNG", baos );
            log.info( LogConstants.CONTROLLER_QR_GENERATED_SUCCESS, image.getWidth(), image.getHeight() );
            return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( baos.toByteArray() );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.CONTROLLER_ERROR_GENERATING_QR, e );
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = ControllerConstants.QR_DEBUG_STYLED_PATH, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateStyledQR( @RequestParam(name = ControllerConstants.PARAM_DATA, defaultValue = ControllerConstants.DEFAULT_QR_DATA) String data,
                                                    @RequestParam(name = ControllerConstants.PARAM_SIZE, defaultValue = "512") int size,
                                                    @RequestParam(name = ControllerConstants.PARAM_BODY_SHAPE, defaultValue = "ROUNDED") String bodyShape,
                                                    @RequestParam(name = ControllerConstants.PARAM_COLOR, defaultValue = "#0066FF") String color )
    {
        try
        {
            log.info( LogConstants.CONTROLLER_GENERATING_STYLED_QR, data, size, bodyShape, color );
            // Create styled config using record constructor
            QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.valueOf( bodyShape.toUpperCase() ),
                                                        color,
                                                        ControllerConstants.DEFAULT_BACKGROUND_COLOR,
                                                        true );
            QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.ROUNDED,
                                                     color,
                                                     ControllerConstants.DEFAULT_BACKGROUND_COLOR );
            QRConfig config = new QRConfig( data,
                                            ControllerConstants.DEFAULT_QR_SIZE,
                                            ControllerConstants.DEFAULT_STYLED_MARGIN,
                                            ControllerConstants.DEFAULT_ERROR_CORRECTION,
                                            ControllerConstants.DEFAULT_TRANSPARENT,
                                            ControllerConstants.DEFAULT_WHITE_COLOR,
                                            bodyConfig,
                                            eyeConfig,
                                            null );
            log.info( LogConstants.CONTROLLER_STYLED_CONFIG_CREATED, config );
            BufferedImage image = zapQrEngine.generate( config );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "PNG", baos );
            log.info( LogConstants.CONTROLLER_STYLED_QR_GENERATED );
            return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( baos.toByteArray() );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.CONTROLLER_ERROR_GENERATING_STYLED_QR, e );
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(ControllerConstants.QR_DEBUG_TEST_CONFIG_PATH)
    public ResponseEntity<QRConfig> getTestConfig()
    {
        QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.ROUNDED,
                                                    ControllerConstants.DEFAULT_COLOR,
                                                    ControllerConstants.DEFAULT_BACKGROUND_COLOR,
                                                    true );
        QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.ROUNDED,
                                                 ControllerConstants.DEFAULT_COLOR,
                                                 ControllerConstants.DEFAULT_BACKGROUND_COLOR );
        QRConfig config = new QRConfig( ControllerConstants.DEFAULT_QR_DATA,
                                        ControllerConstants.DEFAULT_QR_SIZE,
                                        ControllerConstants.DEFAULT_STYLED_MARGIN,
                                        ControllerConstants.DEFAULT_ERROR_CORRECTION,
                                        ControllerConstants.DEFAULT_TRANSPARENT,
                                        ControllerConstants.DEFAULT_WHITE_COLOR,
                                        bodyConfig,
                                        eyeConfig,
                                        null );
        return ResponseEntity.ok( config );
    }

    @GetMapping(ControllerConstants.QR_DEBUG_VERIFY_PATH)
    public ResponseEntity<java.util.Map<String, Object>> verifyQR( @RequestParam(name = ControllerConstants.PARAM_DATA, defaultValue = "TEST") String data )
    {
        try
        {
            log.info( LogConstants.CONTROLLER_VERIFYING_QR_GENERATION, data );
            // Generate QR using record constructor
            QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.SQUARE,
                                                        ControllerConstants.DEFAULT_FOREGROUND_COLOR,
                                                        null,
                                                        true );
            QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.SQUARE,
                                                     ControllerConstants.DEFAULT_FOREGROUND_COLOR,
                                                     ControllerConstants.DEFAULT_FOREGROUND_COLOR );
            QRConfig config = new QRConfig( data,
                                            ControllerConstants.DEFAULT_QR_SIZE,
                                            ControllerConstants.DEFAULT_MARGIN,
                                            ControllerConstants.DEFAULT_ERROR_CORRECTION,
                                            ControllerConstants.DEFAULT_TRANSPARENT,
                                            ControllerConstants.DEFAULT_WHITE_COLOR,
                                            bodyConfig,
                                            eyeConfig,
                                            null );
            BufferedImage image = zapQrEngine.generate( config );
            // Decode it back using ZXing
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource( image );
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap( new com.google.zxing.common.HybridBinarizer( source ) );
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode( bitmap );
            String decodedData = result.getText();
            log.info( LogConstants.QR_VERIFICATION_SUCCESS, data, decodedData );
            return ResponseEntity.ok( java.util.Map
                    .of( MessageConstants.RESPONSE_KEY_SUCCESS, true, MessageConstants.RESPONSE_KEY_ORIGINAL_DATA, data,
                         MessageConstants.RESPONSE_KEY_DECODED_DATA, decodedData, MessageConstants.RESPONSE_KEY_MATCH,
                         data.equals( decodedData ) ) );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.QR_VERIFICATION_FAILED, e );
            return ResponseEntity.ok( java.util.Map.of( MessageConstants.RESPONSE_KEY_SUCCESS, false,
                                                        MessageConstants.RESPONSE_KEY_ORIGINAL_DATA, data,
                                                        MessageConstants.RESPONSE_KEY_ERROR, e.getMessage() ) );
        }
    }

    // Baseline test using ZXing's native renderer (should always work)
    @GetMapping(ControllerConstants.QR_DEBUG_VERIFY_NATIVE_PATH)
    public ResponseEntity<java.util.Map<String, Object>> verifyNativeQR( @RequestParam(name = ControllerConstants.PARAM_DATA, defaultValue = "TEST") String data )
    {
        try
        {
            log.info( LogConstants.CONTROLLER_VERIFYING_NATIVE_QR, data );
            // Generate BitMatrix using ZXing
            java.util.Map<com.google.zxing.EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put( com.google.zxing.EncodeHintType.ERROR_CORRECTION,
                       com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H );
            hints.put( com.google.zxing.EncodeHintType.CHARACTER_SET, MessageConstants.ENCODING_UTF_8 );
            hints.put( com.google.zxing.EncodeHintType.MARGIN, ControllerConstants.DEFAULT_MARGIN );
            com.google.zxing.common.BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter()
                    .encode( data, com.google.zxing.BarcodeFormat.QR_CODE, ControllerConstants.DEFAULT_QR_SIZE,
                             ControllerConstants.DEFAULT_QR_SIZE, hints );
            // Use ZXing's NATIVE renderer (MatrixToImageWriter)
            BufferedImage image = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage( bitMatrix );
            // Decode it back
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource( image );
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap( new com.google.zxing.common.HybridBinarizer( source ) );
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode( bitmap );
            String decodedData = result.getText();
            log.info( LogConstants.QR_NATIVE_VERIFICATION_SUCCESS, data, decodedData );
            return ResponseEntity.ok( java.util.Map
                    .of( MessageConstants.RESPONSE_KEY_SUCCESS, true, MessageConstants.RESPONSE_KEY_RENDERER,
                         MessageConstants.RENDERER_NATIVE_ZXING, MessageConstants.RESPONSE_KEY_ORIGINAL_DATA, data,
                         MessageConstants.RESPONSE_KEY_DECODED_DATA, decodedData, MessageConstants.RESPONSE_KEY_MATCH,
                         data.equals( decodedData ) ) );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.QR_NATIVE_VERIFICATION_FAILED, e );
            return ResponseEntity.ok( java.util.Map
                    .of( MessageConstants.RESPONSE_KEY_SUCCESS, false, MessageConstants.RESPONSE_KEY_RENDERER,
                         MessageConstants.RENDERER_NATIVE_ZXING, MessageConstants.RESPONSE_KEY_ORIGINAL_DATA, data,
                         MessageConstants.RESPONSE_KEY_ERROR, e.getMessage() ) );
        }
    }

    // Verify styled QR codes with custom shapes
    @GetMapping(ControllerConstants.QR_DEBUG_VERIFY_STYLED_PATH)
    public ResponseEntity<java.util.Map<String, Object>> verifyStyledQR( @RequestParam(name = ControllerConstants.PARAM_DATA, defaultValue = "TEST") String data,
                                                                         @RequestParam(name = ControllerConstants.PARAM_BODY_SHAPE, defaultValue = "SQUARE") String bodyShape,
                                                                         @RequestParam(name = ControllerConstants.PARAM_EYE_SHAPE, defaultValue = "SQUARE") String eyeShape )
    {
        try
        {
            log.info( LogConstants.CONTROLLER_VERIFYING_STYLED_QR, data, bodyShape, eyeShape );
            // Create styled config using record constructor
            QRBodyConfig bodyConfig = new QRBodyConfig( QRBodyShapeEnum.valueOf( bodyShape.toUpperCase() ),
                                                        ControllerConstants.DEFAULT_FOREGROUND_COLOR,
                                                        null,
                                                        true );
            QREyeConfig eyeConfig = new QREyeConfig( QREyeShapeEnum.valueOf( eyeShape.toUpperCase() ),
                                                     ControllerConstants.DEFAULT_FOREGROUND_COLOR,
                                                     ControllerConstants.DEFAULT_FOREGROUND_COLOR );
            QRConfig config = new QRConfig( data,
                                            ControllerConstants.DEFAULT_QR_SIZE,
                                            ControllerConstants.DEFAULT_MARGIN,
                                            ControllerConstants.DEFAULT_ERROR_CORRECTION,
                                            ControllerConstants.DEFAULT_TRANSPARENT,
                                            ControllerConstants.DEFAULT_WHITE_COLOR,
                                            bodyConfig,
                                            eyeConfig,
                                            null );
            BufferedImage image = zapQrEngine.generate( config );
            // Decode it back
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource( image );
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap( new com.google.zxing.common.HybridBinarizer( source ) );
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode( bitmap );
            String decodedData = result.getText();
            log.info( LogConstants.QR_STYLED_VERIFICATION_SUCCESS, bodyShape, eyeShape, decodedData );
            return ResponseEntity.ok( java.util.Map
                    .of( MessageConstants.RESPONSE_KEY_SUCCESS, true, MessageConstants.RESPONSE_KEY_BODY_SHAPE,
                         bodyShape, MessageConstants.RESPONSE_KEY_EYE_SHAPE, eyeShape,
                         MessageConstants.RESPONSE_KEY_ORIGINAL_DATA, data, MessageConstants.RESPONSE_KEY_DECODED_DATA,
                         decodedData, MessageConstants.RESPONSE_KEY_MATCH, data.equals( decodedData ) ) );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.QR_STYLED_VERIFICATION_FAILED, bodyShape, eyeShape, e );
            return ResponseEntity.ok( java.util.Map
                    .of( MessageConstants.RESPONSE_KEY_SUCCESS, false, MessageConstants.RESPONSE_KEY_BODY_SHAPE,
                         bodyShape, MessageConstants.RESPONSE_KEY_EYE_SHAPE, eyeShape,
                         MessageConstants.RESPONSE_KEY_ORIGINAL_DATA, data, MessageConstants.RESPONSE_KEY_ERROR,
                         e.getMessage() != null ? e.getMessage() : "NotFoundException" ) );
        }
    }
}
