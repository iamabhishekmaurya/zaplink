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
import io.zaplink.core.dto.request.UpdateShortLinkRequest;
import io.zaplink.core.dto.request.biolink.CreateBioLinkRequest;
import io.zaplink.core.dto.request.biolink.ReorderLinksRequest;
import io.zaplink.core.dto.request.biolink.UpdateBioLinkRequest;
import io.zaplink.core.dto.request.biopage.CreateBioPageRequest;
import io.zaplink.core.dto.request.biopage.UpdateBioPageRequest;
import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import io.zaplink.core.dto.response.biopage.BioPageResponse;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.service.BioLinkService;
import io.zaplink.core.service.BioPageService;
import io.zaplink.core.service.DynamicQrService;
import io.zaplink.core.service.QRService;
import io.zaplink.core.service.UrlShortnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping("/core")
public class CoreController
{
    private final UrlShortnerService urlServiceProvider;
    private final DynamicQrService   dynamicQrService;
    private final QRService          qrService;
    private final BioPageService     bioPageWriteService;
    private final BioLinkService     bioLinkWriteService;
    @PostMapping(value = "/url")
    public ShortnerResponse createShortUrl( @Valid @RequestBody ShortnerRequest urlRequest,
                                            @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlServiceProvider.createShortUrl( urlRequest, userEmail );
    }

    @PutMapping(value = "/url")
    public ShortnerResponse updateShortUrl( @Valid @RequestBody UpdateShortLinkRequest updateRequest,
                                            @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlServiceProvider.updateShortUrl( updateRequest, userEmail );
    }

    @PostMapping(value = "/dyqr")
    public ResponseEntity<DynamicQrResponse> createDynamicQr( @Valid @RequestBody CreateDynamicQrRequest request,
                                                              @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.createDynamicQr( request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping(value = "/dyqr/{qrKey}")
    public ResponseEntity<DynamicQrResponse> updateDynamicQr( @PathVariable("qrKey") String qrKey,
                                                              @Valid @RequestBody io.zaplink.core.dto.request.dynamicqr.UpdateDynamicQrRequest request,
                                                              @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.updateDynamicQr( qrKey, request, userEmail );
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
    // ========== BioPage Write Operations (CQRS Command Side) ==========

    @PostMapping(value = "/bio-pages")
    public BioPageResponse createBioPage( @Valid @RequestBody CreateBioPageRequest request,
                                          @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return bioPageWriteService.createBioPage( request, userEmail );
    }

    @PutMapping(value = "/bio-pages/{pageId}")
    public BioPageResponse updateBioPage( @PathVariable Long pageId,
                                          @Valid @RequestBody UpdateBioPageRequest request,
                                          @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return bioPageWriteService.updateBioPage( pageId, request, userEmail );
    }

    @DeleteMapping(value = "/bio-pages/{pageId}")
    public ResponseEntity<Void> deleteBioPage( @PathVariable Long pageId,
                                               @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        boolean deleted = bioPageWriteService.deleteBioPage( pageId, userEmail );
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    // ========== BioLink Write Operations (CQRS Command Side) ==========

    @PostMapping(value = "/bio-links")
    public BioLinkResponse createBioLink( @Valid @RequestBody CreateBioLinkRequest request,
                                          @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return bioLinkWriteService.createBioLink( request, userEmail );
    }

    @PutMapping(value = "/bio-links/{linkId}")
    public BioLinkResponse updateBioLink( @PathVariable Long linkId,
                                          @Valid @RequestBody UpdateBioLinkRequest request,
                                          @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return bioLinkWriteService.updateBioLink( linkId, request, userEmail );
    }

    @DeleteMapping(value = "/bio-links/{linkId}")
    public ResponseEntity<Void> deleteBioLink( @PathVariable("linkId") Long linkId,
                                               @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        boolean deleted = bioLinkWriteService.deleteBioLink( linkId, userEmail );
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/bio-links/{pageId}/reorder")
    public ResponseEntity<Void> reorderLinks( @Valid @RequestBody ReorderLinksRequest request,
                                              @PathVariable("pageId") Long pageId,
                                              @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        boolean reordered = bioLinkWriteService.reorderLinks( pageId, request, userEmail );
        return reordered ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
