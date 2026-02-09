package io.zaplink.core.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import io.zaplink.core.common.constants.ControllerConstants;
import io.zaplink.core.common.constants.StatusConstants;
import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.request.UpdateShortLinkRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.service.UrlShortnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping(ControllerConstants.SHORT_BASE_PATH) @Tag(name = ControllerConstants.TAG_URL_SHORTENER, description = ControllerConstants.TAG_URL_SHORTENER_DESC)
public class ShortUrlController
{
  private final UrlShortnerService urlServiceProvider;
  @PostMapping(value = ControllerConstants.SHORT_URL_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.CORE_CREATE_SHORT_URL_SUMMARY, description = ControllerConstants.CORE_CREATE_SHORT_URL_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_SHORT_URL_CREATED, content = @Content(schema = @Schema(implementation = ShortnerResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_URL_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_SHORT_URL_EXISTS) })
  public ShortnerResponse createShortUrl( @Valid @RequestBody ShortnerRequest urlRequest,
                                          @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return urlServiceProvider.createShortUrl( urlRequest, userEmail );
  }

  @PutMapping(value = ControllerConstants.SHORT_URL_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.CORE_UPDATE_SHORT_URL_SUMMARY, description = ControllerConstants.CORE_UPDATE_SHORT_URL_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_SHORT_URL_UPDATED, content = @Content(schema = @Schema(implementation = ShortnerResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_URL_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_SHORT_URL_NOT_FOUND) })
  public ShortnerResponse updateShortUrl( @Valid @RequestBody UpdateShortLinkRequest updateRequest,
                                          @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return urlServiceProvider.updateShortUrl( updateRequest, userEmail );
  }

  @PatchMapping(value = ControllerConstants.SHORT_URL_STATUS_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.CORE_TOGGLE_SHORT_URL_STATUS_SUMMARY, description = ControllerConstants.CORE_TOGGLE_SHORT_URL_STATUS_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_SHORT_URL_STATUS_TOGGLED, content = @Content(schema = @Schema(implementation = ShortnerResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_SHORT_URL_NOT_FOUND) })
  public ShortnerResponse toggleShortUrlStatus( @Parameter(description = "Short URL key") @PathVariable("shortUrlKey") String shortUrlKey,
                                                @Parameter(description = "Active status to set") @RequestParam("active") boolean active,
                                                @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return urlServiceProvider.toggleShortUrlStatus( shortUrlKey, active, userEmail );
  }

  @DeleteMapping(value = ControllerConstants.SHORT_URL_PATH + "/{shortUrlKey}", version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.CORE_DELETE_SHORT_URL_SUMMARY, description = ControllerConstants.CORE_DELETE_SHORT_URL_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_SHORT_URL_DELETED, content = @Content(schema = @Schema(implementation = ShortnerResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_SHORT_URL_NOT_FOUND) })
  public ShortnerResponse deleteShortUrlByKey( @Parameter(description = "Short URL key") @PathVariable("shortUrlKey") String shortUrlKey,
                                               @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return urlServiceProvider.deleteShortUrlByKey( shortUrlKey, userEmail );
  }
}
