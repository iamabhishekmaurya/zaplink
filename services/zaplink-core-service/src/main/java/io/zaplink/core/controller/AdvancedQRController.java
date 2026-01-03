package io.zaplink.core.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.service.impl.AdvancedQRServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${api.base-path}/qr/advanced")
@RequiredArgsConstructor
@Slf4j
public class AdvancedQRController {
    private final AdvancedQRServiceImpl advancedQRService;
    
    @PostMapping(value = "/styled", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateStyledQR( @RequestBody QRConfig config )
    {
        try
        {
            log.info( "Received styled QR request with margin: {}", config.getMargin() );
            byte[] qrImage = advancedQRService.generateStyledQrCode( config );
            return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( qrImage );
        }
        catch ( Exception e )
        {
            log.error( "Error generating styled QR code", e );
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get available options for customization
    @GetMapping("/options")
    public ResponseEntity<Map<String, Object>> getQROptions()
    {
        return ResponseEntity.ok( Map.of( "bodyShapes", QRBodyShapeEnum.values(), "eyeShapes", QREyeShapeEnum.values(),
                                          "gradientTypes", Map.of( "linear", true, "radial", false ), "exampleColors",
                                          Map.of( "blue", "#0066FF", "red", "#FF6B6B", "green", "#4ECDC4", "purple",
                                                  "#6366F1", "black", "#000000", "darkGray", "#1A1A1A" ) ) );
    }
}
