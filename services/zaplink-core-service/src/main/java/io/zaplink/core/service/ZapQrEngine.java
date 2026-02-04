package io.zaplink.core.service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.constants.MessageConstants;
import io.zaplink.core.dto.request.qr.QRConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class ZapQrEngine
{
    private final QRRenderer renderer;
    public BufferedImage generate( QRConfig config )
    {
        try
        {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put( EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H );
            hints.put( EncodeHintType.CHARACTER_SET, MessageConstants.ENCODING_UTF_8 );
            hints.put( EncodeHintType.MARGIN, config.margin() ); // Default 1 usually
            log.info( LogConstants.QR_ZXING_MARGIN_HINT_SET, config.margin() );
            // Let ZXing determine the smallest version that fits the data
            // Passing 0, 0 instructs the writer to just wrap the content
            BitMatrix bitMatrix = new MultiFormatWriter().encode( config.data(), BarcodeFormat.QR_CODE, 0, 0, hints );
            log.info( LogConstants.QR_GENERATED_MATRIX, bitMatrix.getWidth(), bitMatrix.getHeight(), config.data() );
            // Dynamic detection of ZXing's enforced margin
            int[] topLeft = bitMatrix.getTopLeftOnBit();
            if ( topLeft != null )
            {
                int zxingMargin = topLeft[0];
                if ( zxingMargin > config.margin() )
                {
                    log.info( LogConstants.QR_ZXING_ENFORCED_MARGIN, zxingMargin, config.margin() );
                    // If we requested 0 (or less than what we got) and got more, crop IF user wanted 0
                    // The original logic was specifically targeting config.margin() == 0
                    if ( config.margin() == 0 && zxingMargin > 0 )
                    {
                        log.info( LogConstants.QR_CROPPING_MATRIX );
                        bitMatrix = cropMatrix( bitMatrix, zxingMargin );
                        log.info( LogConstants.QR_CROPPED_MATRIX_SIZE, bitMatrix.getWidth(), bitMatrix.getHeight() );
                    }
                }
            }
            return renderer.render( bitMatrix, config );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.QR_FAILED_TO_GENERATE_ADVANCED, e );
            throw new RuntimeException( ErrorConstant.ERROR_ADV_QR_GENERATION_FAILED, e );
        }
    }

    // Helper method to crop BitMatrix to remove enforced margin
    private BitMatrix cropMatrix( BitMatrix matrix, int marginToRemove )
    {
        int originalSize = matrix.getWidth();
        int newSize = originalSize - ( marginToRemove * 2 );
        if ( newSize <= 0 )
        {
            log.warn( LogConstants.QR_CANNOT_CROP_MATRIX, newSize, newSize );
            return matrix;
        }
        BitMatrix cropped = new BitMatrix( newSize );
        for ( int y = 0; y < newSize; y++ )
        {
            for ( int x = 0; x < newSize; x++ )
            {
                // Copy from original matrix with offset
                if ( matrix.get( x + marginToRemove, y + marginToRemove ) )
                {
                    cropped.set( x, y );
                }
            }
        }
        return cropped;
    }
}
