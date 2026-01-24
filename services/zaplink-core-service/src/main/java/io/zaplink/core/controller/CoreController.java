package io.zaplink.core.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.service.dynamicqr.DynamicQrService;
import io.zaplink.core.service.qr.QRService;
import io.zaplink.core.service.shortner.UrlShortnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping("/core")
public class CoreController
{
    private final UrlShortnerService urlServiceProvider;
    private final DynamicQrService   dynamicQrService;
    private final QRService          qrService;
    @PostMapping(value = "/url")
    public ShortnerResponse createShortUrl( @Valid @RequestBody ShortnerRequest urlRequest,
                                            @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlServiceProvider.createShortUrl( urlRequest, userEmail );
    }

    @PostMapping(value = "/dyqr")
    public ResponseEntity<DynamicQrResponse> createDynamicQr( @Valid @RequestBody CreateDynamicQrRequest request,
                                                              @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.createDynamicQr( request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping(value = "/dyqr/{qrKey}/destination")
    public ResponseEntity<DynamicQrResponse> updateDestination( @PathVariable("qrKey") String qrKey,
                                                                @Valid @RequestBody UpdateDestinationRequest request,
                                                                @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.updateDestination( qrKey, request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping(value = "/dyqr/{qrKey}/status")
    public ResponseEntity<Void> toggleQrStatus( @PathVariable("qrKey") String qrKey,
                                                @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        dynamicQrService.toggleQrStatus( qrKey, userEmail );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/dyqr/{qrKey}")
    public ResponseEntity<Void> deleteDynamicQr( @PathVariable("qrKey") String qrKey,
                                                 @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        dynamicQrService.deleteDynamicQr( qrKey, userEmail );
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/qr/styled", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateStyledQR( @RequestBody QRConfig config )
    {
        byte[] qrImage = qrService.generateStyledQrCode( config );
        return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( qrImage );
    }

    @GetMapping(value = "/qr/options")
    public ResponseEntity<Map<String, Object>> getQROptions()
    {
        return ResponseEntity.ok( Map.of( "bodyShapes", QRBodyShapeEnum.values(), "eyeShapes", QREyeShapeEnum.values(),
                                          "gradientTypes", Map.of( "linear", true, "radial", false ), "exampleColors",
                                          Map.of( "blue", "#0066FF", "red", "#FF6B6B", "green", "#4ECDC4", "purple",
                                                  "#6366F1", "black", "#000000", "darkGray", "#1A1A1A" ) ) );
    }
}
