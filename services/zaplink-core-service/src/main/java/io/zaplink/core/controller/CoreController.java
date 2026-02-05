package io.zaplink.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.core.common.constants.ControllerConstants;
import io.zaplink.core.common.constants.StatusConstants;
import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.request.UpdateShortLinkRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.service.UrlShortnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping(ControllerConstants.CORE_BASE_PATH) @Tag(name = ControllerConstants.TAG_URL_SHORTENER, description = ControllerConstants.TAG_URL_SHORTENER_DESC)
public class CoreController
{
    private final UrlShortnerService urlServiceProvider;
    @PostMapping(value = ControllerConstants.CORE_URL_PATH) @Operation(summary = ControllerConstants.CORE_CREATE_SHORT_URL_SUMMARY, description = ControllerConstants.CORE_CREATE_SHORT_URL_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_SHORT_URL_CREATED, content = @Content(schema = @Schema(implementation = ShortnerResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_URL_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_SHORT_URL_EXISTS) })
    public ShortnerResponse createShortUrl( @Valid @RequestBody ShortnerRequest urlRequest,
                                            @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        return urlServiceProvider.createShortUrl( urlRequest, userEmail );
    }

    @PutMapping(value = ControllerConstants.CORE_URL_PATH
            + "/{shortUrl}") @Operation(summary = ControllerConstants.CORE_UPDATE_SHORT_URL_SUMMARY, description = ControllerConstants.CORE_UPDATE_SHORT_URL_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_SHORT_URL_UPDATED, content = @Content(schema = @Schema(implementation = ShortnerResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_URL_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_SHORT_URL_NOT_FOUND) })
    public ShortnerResponse updateShortUrl( @Valid @RequestBody UpdateShortLinkRequest updateRequest,
                                            @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        return urlServiceProvider.updateShortUrl( updateRequest, userEmail );
    }
}
