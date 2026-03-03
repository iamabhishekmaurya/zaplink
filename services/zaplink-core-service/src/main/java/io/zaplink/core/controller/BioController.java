package io.zaplink.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import io.zaplink.core.dto.request.biolink.CreateBioLinkRequest;
import io.zaplink.core.dto.request.biolink.ReorderLinksRequest;
import io.zaplink.core.dto.request.biolink.TrackClickRequest;
import io.zaplink.core.dto.request.biolink.UpdateBioLinkRequest;
import io.zaplink.core.dto.request.biopage.CreateBioPageRequest;
import io.zaplink.core.dto.request.biopage.UpdateBioPageRequest;
import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import io.zaplink.core.dto.response.biopage.BioPageResponse;
import io.zaplink.core.service.BioLinkService;
import io.zaplink.core.service.BioPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for bio link operations.
 * Provides endpoints for bio link management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@RestController @RequestMapping(ControllerConstants.BIO_BASE_PATH) @RequiredArgsConstructor @Validated @Tag(name = ControllerConstants.TAG_BIO_MANAGEMENT, description = ControllerConstants.TAG_BIO_MANAGEMENT_DESC)
public class BioController
{
  private final BioPageService bioPageWriteService;
  private final BioLinkService bioLinkWriteService;
  // ========== BioPage Write Operations (CQRS Command Side) ==========
  @PostMapping(value = ControllerConstants.BIO_PAGE_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_CREATE_PAGE_SUMMARY, description = ControllerConstants.BIO_CREATE_PAGE_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_PAGE_CREATED, content = @Content(schema = @Schema(implementation = BioPageResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_USERNAME_EXISTS) })
  public BioPageResponse createBioPage( @Valid @RequestBody CreateBioPageRequest request,
                                        @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return bioPageWriteService.createBioPage( request, userEmail );
  }

  @PutMapping(value = ControllerConstants.BIO_PAGE_PATH
      + "/{pageId}", version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_UPDATE_PAGE_SUMMARY, description = ControllerConstants.BIO_UPDATE_PAGE_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_PAGE_UPDATED, content = @Content(schema = @Schema(implementation = BioPageResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
  public BioPageResponse updateBioPage( @Parameter(description = ControllerConstants.PARAM_DESC_BIO_PAGE_ID) @PathVariable("pageId") Long pageId,
                                        @Valid @RequestBody UpdateBioPageRequest request,
                                        @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return bioPageWriteService.updateBioPage( pageId, request, userEmail );
  }

  @DeleteMapping(value = ControllerConstants.BIO_PAGE_PATH
      + "/{pageId}", version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_DELETE_PAGE_SUMMARY, description = ControllerConstants.BIO_DELETE_PAGE_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_PAGE_DELETED),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
  public ResponseEntity<Void> deleteBioPage( @Parameter(description = ControllerConstants.PARAM_DESC_BIO_PAGE_ID) @PathVariable("pageId") Long pageId,
                                             @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    boolean deleted = bioPageWriteService.deleteBioPage( pageId, userEmail );
    return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  // ========== BioLink Write Operations (CQRS Command Side) ==========
  @PostMapping(value = ControllerConstants.BIO_LINK_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_CREATE_LINK_SUMMARY, description = ControllerConstants.BIO_CREATE_LINK_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_LINK_CREATED, content = @Content(schema = @Schema(implementation = BioLinkResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
  public BioLinkResponse createBioLink( @Valid @RequestBody CreateBioLinkRequest request,
                                        @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return bioLinkWriteService.createBioLink( request, userEmail );
  }

  @PutMapping(value = ControllerConstants.BIO_LINK_PATH
      + "/{linkId}", version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_UPDATE_LINK_SUMMARY, description = ControllerConstants.BIO_UPDATE_LINK_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_LINK_UPDATED, content = @Content(schema = @Schema(implementation = BioLinkResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_LINK_NOT_FOUND) })
  public BioLinkResponse updateBioLink( @Parameter(description = ControllerConstants.PARAM_DESC_BIO_LINK_ID) @PathVariable("linkId") Long linkId,
                                        @Valid @RequestBody UpdateBioLinkRequest request,
                                        @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    return bioLinkWriteService.updateBioLink( linkId, request, userEmail );
  }

  @DeleteMapping(value = ControllerConstants.BIO_LINK_PATH
      + "/{linkId}", version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_DELETE_LINK_SUMMARY, description = ControllerConstants.BIO_DELETE_LINK_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_LINK_DELETED),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_LINK_NOT_FOUND) })
  public ResponseEntity<Void> deleteBioLink( @Parameter(description = ControllerConstants.PARAM_DESC_BIO_LINK_ID) @PathVariable("linkId") Long linkId,
                                             @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    boolean deleted = bioLinkWriteService.deleteBioLink( linkId, userEmail );
    return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @PutMapping(value = ControllerConstants.BIO_LINK_REORDER_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.BIO_REORDER_LINKS_SUMMARY, description = ControllerConstants.BIO_REORDER_LINKS_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_LINKS_REORDERED),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_REORDER_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
  public ResponseEntity<Void> reorderLinks( @Valid @RequestBody ReorderLinksRequest request,
                                            @Parameter(description = ControllerConstants.PARAM_DESC_BIO_PAGE_ID) @PathVariable("pageId") Long pageId,
                                            @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
  {
    boolean reordered = bioLinkWriteService.reorderLinks( pageId, request, userEmail );
    return reordered ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }

  @PostMapping(value = ControllerConstants.BIO_LINK_PATH
      + "/{linkId}/click", version = ControllerConstants.API_VERSION_1) @Operation(summary = "Track Link Click", description = "Records a click event for a bio link") @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = "Click tracked successfully"),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_LINK_NOT_FOUND) })
  public ResponseEntity<Void> trackClick( @Parameter(description = ControllerConstants.PARAM_DESC_BIO_LINK_ID) @PathVariable("linkId") Long linkId,
                                          @RequestBody(required = false) TrackClickRequest request,
                                          HttpServletRequest servletRequest )
  {
    String ipAddress = servletRequest.getRemoteAddr();
    bioLinkWriteService.trackClick( linkId, request, ipAddress );
    return ResponseEntity.ok().build();
  }
}
