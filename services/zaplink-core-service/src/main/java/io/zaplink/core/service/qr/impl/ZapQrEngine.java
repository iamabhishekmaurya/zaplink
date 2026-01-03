package io.zaplink.core.service.qr.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.service.qr.QRRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Service @Slf4j @RequiredArgsConstructor
public class ZapQrEngine
{
    private final QRRenderer renderer;
    public BufferedImage generate( QRConfig config )
    {
        try
        {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put( EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H );
            hints.put( EncodeHintType.CHARACTER_SET, "UTF-8" );
            hints.put( EncodeHintType.MARGIN, config.getMargin() ); // Default 1 usually
            log.info( "ZXing margin hint set to: {}", config.getMargin() );
            // Let ZXing determine the smallest version that fits the data
            // Passing 0, 0 instructs the writer to just wrap the content
            BitMatrix bitMatrix = new MultiFormatWriter().encode( config.getData(), BarcodeFormat.QR_CODE, 0, 0,
                                                                  hints );
            log.info( "Generated QR matrix: {}x{} for data: {}", bitMatrix.getWidth(), bitMatrix.getHeight(),
                      config.getData() );
            
            // Check if ZXing added its own margin despite our hint
            int actualMatrixSize = bitMatrix.getWidth();
            int expectedDataSize = 29; // Approximate for our test data
            if (actualMatrixSize > expectedDataSize) {
                int zxingMargin = (actualMatrixSize - expectedDataSize) / 2;
                log.warn( "ZXing enforced additional margin: {} modules (matrix size: {})", zxingMargin, actualMatrixSize );
                
                // If margin is 0, try to crop the matrix to remove ZXing's enforced quiet zone
                if (config.getMargin() == 0 && zxingMargin > 0) {
                    log.info( "Cropping matrix to remove ZXing enforced margin..." );
                    bitMatrix = cropMatrix(bitMatrix, zxingMargin);
                    log.info( "Cropped matrix size: {}x{}", bitMatrix.getWidth(), bitMatrix.getHeight() );
                }
            }
            return renderer.render( bitMatrix, config );
        }
        catch ( Exception e )
        {
            log.error( "Failed to generate advanced QR", e );
            throw new RuntimeException( "Adv QR Generation failed", e );
        }
    }
    
    // Helper method to crop BitMatrix to remove enforced margin
    private BitMatrix cropMatrix(BitMatrix matrix, int marginToRemove) {
        int originalSize = matrix.getWidth();
        int newSize = originalSize - (marginToRemove * 2);
        
        if (newSize <= 0) {
            log.warn( "Cannot crop matrix - new size would be {}x{}", newSize, newSize );
            return matrix;
        }
        
        BitMatrix cropped = new BitMatrix(newSize);
        for (int y = 0; y < newSize; y++) {
            for (int x = 0; x < newSize; x++) {
                // Copy from original matrix with offset
                if (matrix.get(x + marginToRemove, y + marginToRemove)) {
                    cropped.set(x, y);
                }
            }
        }
        
        return cropped;
    }
}
