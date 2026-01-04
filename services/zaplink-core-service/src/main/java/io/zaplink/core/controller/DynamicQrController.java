package io.zaplink.core.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.core.service.dynamicqr.DynamicQrService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Validated @RestController @RequiredArgsConstructor @RequestMapping("${api.base-path}/dynamic-qr")
public class DynamicQrController
{
    private final DynamicQrService dynamicQrService;
    @GetMapping("/test")
    public ResponseEntity<String> test()
    {
        return ResponseEntity.ok( "Dynamic QR Controller is working!" );
    }

    @GetMapping("/test-db")
    public ResponseEntity<String> testDatabase()
    {
        try
        {
            long count = dynamicQrService.countAllQrCodes();
            return ResponseEntity.ok( "Database connection working! Total QR codes: " + count );
        }
        catch ( Exception e )
        {
            log.error( "Database test failed", e );
            return ResponseEntity.status( 500 ).body( "Database test failed: " + e.getMessage() );
        }
    }

    @PostMapping
    public ResponseEntity<DynamicQrResponse> createDynamicQr( @Valid @RequestBody CreateDynamicQrRequest request,
                                                              @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.createDynamicQr( request, userEmail );
        return ResponseEntity.ok( response );
    }

    @GetMapping("/{qrKey}")
    public ResponseEntity<DynamicQrResponse> getDynamicQr( @PathVariable("qrKey") String qrKey,
                                                           @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        Optional<DynamicQrResponse> response = dynamicQrService.getDynamicQr( qrKey, userEmail );
        return response.map( ResponseEntity::ok ).orElse( ResponseEntity.notFound().build() );
    }

    @GetMapping
    public ResponseEntity<Page<DynamicQrResponse>> getDynamicQrsByUser( @RequestHeader(value = "X-User-Email", required = false) String userEmail,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "10") int size )
    {
        Pageable pageable = PageRequest.of( page, size );
        Page<DynamicQrResponse> response = dynamicQrService.getDynamicQrsByUser( userEmail, pageable );
        return ResponseEntity.ok( response );
    }

    @PutMapping("/{qrKey}/destination")
    public ResponseEntity<DynamicQrResponse> updateDestination( @PathVariable("qrKey") String qrKey,
                                                                @Valid @RequestBody UpdateDestinationRequest request,
                                                                @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.updateDestination( qrKey, request, userEmail );
        return ResponseEntity.ok( response );
    }

    @GetMapping("/{qrKey}/analytics")
    public ResponseEntity<QrAnalyticsResponse> getQrAnalytics( @PathVariable("qrKey") String qrKey,
                                                               @RequestHeader(value = "X-User-Email", required = false) String userEmail,
                                                               @RequestParam(name = "startDate", required = false) String startDate,
                                                               @RequestParam(name = "endDate", required = false) String endDate )
    {
        LocalDateTime start = null;
        LocalDateTime end = null;
        try
        {
            if ( startDate != null )
                start = LocalDateTime.parse( startDate );
            if ( endDate != null )
                end = LocalDateTime.parse( endDate );
        }
        catch ( Exception e )
        {
            log.warn( "Invalid date format provided for analytics: startDate={}, endDate={}", startDate, endDate );
            // Ignore parse errors and let the service use defaults
        }
        QrAnalyticsResponse response = dynamicQrService.getQrAnalytics( qrKey, userEmail, start, end );
        return ResponseEntity.ok( response );
    }

    @GetMapping(value = "/{qrKey}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQrImage( @PathVariable("qrKey") String qrKey,
                                                   @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        byte[] imageBytes = dynamicQrService.generateQrImage( qrKey, userEmail );
        return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( imageBytes );
    }
}
