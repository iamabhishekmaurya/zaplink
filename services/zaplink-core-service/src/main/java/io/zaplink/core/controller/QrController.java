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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.core.common.constants.ControllerConstants;
import io.zaplink.core.common.constants.StatusConstants;
import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.service.DynamicQrService;
import io.zaplink.core.service.QRService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping(ControllerConstants.QR_BASE_PATH) @RequiredArgsConstructor @Tag(name = ControllerConstants.TAG_QR_MANAGEMENT, description = ControllerConstants.TAG_QR_MANAGEMENT_DESC)
public class QrController
{
    private final DynamicQrService dynamicQrService;
    private final QRService        qrService;
    // ========== Dynamic QR Operations ==========
    @PostMapping(value = ControllerConstants.QR_DYNAMIC_PATH) @Operation(summary = ControllerConstants.QR_CREATE_DYNAMIC_SUMMARY, description = ControllerConstants.QR_CREATE_DYNAMIC_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_DYNAMIC_QR_CREATED, content = @Content(schema = @Schema(implementation = DynamicQrResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_QR_NAME_EXISTS) })
    public ResponseEntity<DynamicQrResponse> createDynamicQr( @Valid @RequestBody CreateDynamicQrRequest request,
                                                              @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.createDynamicQr( request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping(value = ControllerConstants.QR_DYNAMIC_PATH
            + "/{qrKey}") @Operation(summary = ControllerConstants.QR_UPDATE_DYNAMIC_SUMMARY, description = ControllerConstants.QR_UPDATE_DYNAMIC_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_DYNAMIC_QR_UPDATED, content = @Content(schema = @Schema(implementation = DynamicQrResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_DYNAMIC_QR_NOT_FOUND) })
    public ResponseEntity<DynamicQrResponse> updateDynamicQr( @Parameter(description = ControllerConstants.PARAM_DESC_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                              @Valid @RequestBody io.zaplink.core.dto.request.dynamicqr.UpdateDynamicQrRequest request,
                                                              @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.updateDynamicQr( qrKey, request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping(value = ControllerConstants.QR_DESTINATION_PATH) @Operation(summary = ControllerConstants.QR_UPDATE_DESTINATION_SUMMARY, description = ControllerConstants.QR_UPDATE_DESTINATION_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_DESTINATION_UPDATED, content = @Content(schema = @Schema(implementation = DynamicQrResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DESTINATION),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_DYNAMIC_QR_NOT_FOUND) })
    public ResponseEntity<DynamicQrResponse> updateDestination( @Parameter(description = ControllerConstants.PARAM_DESC_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                                @Valid @RequestBody UpdateDestinationRequest request,
                                                                @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.updateDestination( qrKey, request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping(value = ControllerConstants.QR_STATUS_PATH) @Operation(summary = ControllerConstants.QR_TOGGLE_STATUS_SUMMARY, description = ControllerConstants.QR_TOGGLE_STATUS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QR_STATUS_TOGGLED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_DYNAMIC_QR_NOT_FOUND) })
    public ResponseEntity<Void> toggleQrStatus( @Parameter(description = ControllerConstants.PARAM_DESC_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        dynamicQrService.toggleQrStatus( qrKey, userEmail );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = ControllerConstants.QR_DYNAMIC_PATH
            + "/{qrKey}") @Operation(summary = ControllerConstants.QR_DELETE_DYNAMIC_SUMMARY, description = ControllerConstants.QR_DELETE_DYNAMIC_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_DYNAMIC_QR_DELETED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_DYNAMIC_QR_NOT_FOUND) })
    public ResponseEntity<Void> deleteDynamicQr( @Parameter(description = ControllerConstants.PARAM_DESC_QR_KEY) @PathVariable("qrKey") String qrKey,
                                                 @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        dynamicQrService.deleteDynamicQr( qrKey, userEmail );
        return ResponseEntity.ok().build();
    }

    // ========== QR Generation Operations ==========
    @PostMapping(value = ControllerConstants.QR_STYLED_PATH, produces = MediaType.IMAGE_PNG_VALUE) @Operation(summary = ControllerConstants.QR_GENERATE_STYLED_SUMMARY, description = ControllerConstants.QR_GENERATE_STYLED_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QR_IMAGE_GENERATED, content = @Content(mediaType = "image/png")),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_QR_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_500_INTERNAL_SERVER_ERROR, description = ControllerConstants.RESPONSE_400_QR_GENERATION_FAILED) })
    public ResponseEntity<byte[]> generateStyledQR( @RequestBody QRConfig config )
    {
        byte[] qrImage = qrService.generateStyledQrCode( config );
        return ResponseEntity.ok().contentType( MediaType.IMAGE_PNG ).body( qrImage );
    }

    @GetMapping(value = ControllerConstants.QR_OPTIONS_PATH) @Operation(summary = ControllerConstants.QR_GET_OPTIONS_SUMMARY, description = ControllerConstants.QR_GET_OPTIONS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_QR_OPTIONS_RETRIEVED, content = @Content(schema = @Schema(implementation = Map.class))) })
    public ResponseEntity<Map<String, Object>> getQROptions()
    {
        return ResponseEntity.ok( Map.of( "bodyShapes", QRBodyShapeEnum.values(), "eyeShapes", QREyeShapeEnum.values(),
                                          "gradientTypes", Map.of( "linear", true, "radial", false ), "exampleColors",
                                          Map.of( "blue", "#0066FF", "red", "#FF6B6B", "green", "#4ECDC4", "purple",
                                                  "#6366F1", "black", "#000000", "darkGray", "#1A1A1A" ) ) );
    }
}
