package io.zaplink.manager.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.manager.common.constants.ControllerConstants;
import io.zaplink.manager.common.constants.StatusConstants;
import io.zaplink.manager.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.manager.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.manager.service.DynamicQrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController @RequiredArgsConstructor @RequestMapping(ControllerConstants.QR_BASE_PATH) @Tag(name = ControllerConstants.TAG_QR_MANAGEMENT, description = ControllerConstants.TAG_QR_MANAGEMENT_DESC)
public class QrController
{
    private final DynamicQrService dynamicQrService;
    @GetMapping("/test") @Operation(summary = ControllerConstants.QR_TEST_SUMMARY, description = ControllerConstants.QR_TEST_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_TEST_SUCCESS) })
    public ResponseEntity<String> test()
    {
        return ResponseEntity.ok( "Dynamic QR Manager Controller is working!" );
    }

    @GetMapping("/test-db") @Operation(summary = ControllerConstants.QR_TEST_DB_SUMMARY, description = ControllerConstants.QR_TEST_DB_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_DB_TEST_SUCCESS),
      @ApiResponse(responseCode = StatusConstants.STATUS_500_INTERNAL_SERVER_ERROR, description = ControllerConstants.RESPONSE_500_DB_ERROR) })
    public ResponseEntity<String> testDatabase()
    {
        try
        {
            // Test gRPC connectivity by fetching QRs for a test user
            Page<DynamicQrResponse> result = dynamicQrService.getDynamicQrsByUser( "test@test.com",
                                                                                   PageRequest.of( 0, 1 ) );
            return ResponseEntity.ok( "gRPC connection to Core Service working! Test query returned "
                    + result.getTotalElements() + " elements." );
        }
        catch ( Exception e )
        {
            log.error( "gRPC connection test failed", e );
            return ResponseEntity.status( 500 ).body( "gRPC connection test failed: " + e.getMessage() );
        }
    }

    @GetMapping("/{qrKey}") @Operation(summary = ControllerConstants.QR_GET_SUMMARY, description = ControllerConstants.QR_GET_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QR_RETRIEVED, content = @Content(schema = @Schema(implementation = DynamicQrResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_QR_NOT_FOUND) })
    public ResponseEntity<DynamicQrResponse> getDynamicQr( @Parameter(description = ControllerConstants.PARAM_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                           @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        Optional<DynamicQrResponse> response = dynamicQrService.getDynamicQr( qrKey, userEmail );
        return response.map( ResponseEntity::ok ).orElse( ResponseEntity.notFound().build() );
    }

    @GetMapping @Operation(summary = ControllerConstants.QR_GET_BY_USER_SUMMARY, description = ControllerConstants.QR_GET_BY_USER_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QRS_RETRIEVED) })
    public ResponseEntity<Page<DynamicQrResponse>> getDynamicQrsByUser( @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail,
                                                                        @Parameter(description = ControllerConstants.PARAM_PAGE) @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @Parameter(description = ControllerConstants.PARAM_SIZE) @RequestParam(name = "size", defaultValue = "10") int size )
    {
        log.info( "🔍 getDynamicQrsByUser called - X-User-Email header value: '{}'", userEmail );
        Pageable pageable = PageRequest.of( page, size );
        Page<DynamicQrResponse> response = dynamicQrService.getDynamicQrsByUser( userEmail, pageable );
        log.info( "🔍 getDynamicQrsByUser returning {} elements", response.getTotalElements() );
        return ResponseEntity.ok( response );
    }

    @GetMapping("/{qrKey}/analytics") @Operation(summary = ControllerConstants.QR_GET_ANALYTICS_SUMMARY, description = ControllerConstants.QR_GET_ANALYTICS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QR_ANALYTICS_RETRIEVED, content = @Content(schema = @Schema(implementation = QrAnalyticsResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_QR_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATE_FORMAT) })
    public ResponseEntity<QrAnalyticsResponse> getQrAnalytics( @Parameter(description = ControllerConstants.PARAM_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                               @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail,
                                                               @Parameter(description = ControllerConstants.PARAM_START_DATE) @RequestParam(name = "startDate", required = false) String startDate,
                                                               @Parameter(description = ControllerConstants.PARAM_END_DATE) @RequestParam(name = "endDate", required = false) String endDate )
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
        }
        QrAnalyticsResponse response = dynamicQrService.getQrAnalytics( qrKey, userEmail, start, end );
        return ResponseEntity.ok( response );
    }

    @GetMapping(value = "/{qrKey}/image", produces = MediaType.IMAGE_PNG_VALUE) @Operation(summary = ControllerConstants.QR_GENERATE_IMAGE_SUMMARY, description = ControllerConstants.QR_GENERATE_IMAGE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QR_IMAGE_GENERATED, content = @Content(mediaType = "image/png")),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_QR_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_500_INTERNAL_SERVER_ERROR, description = ControllerConstants.RESPONSE_500_QR_GENERATION_ERROR) })
    public ResponseEntity<byte[]> generateQrImage( @Parameter(description = ControllerConstants.PARAM_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                   @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        byte[] imageBytes = dynamicQrService.generateQrImage( qrKey, userEmail );
        return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( imageBytes );
    }
}
