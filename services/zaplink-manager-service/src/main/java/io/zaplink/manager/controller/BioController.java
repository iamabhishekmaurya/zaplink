package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
import io.zaplink.manager.dto.bio.BioLinkResponse;
import io.zaplink.manager.dto.bio.BioPageResponse;
import io.zaplink.manager.service.BioReadService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping(ControllerConstants.BIO_BASE_PATH) @RequiredArgsConstructor @Tag(name = ControllerConstants.TAG_BIO_READ, description = ControllerConstants.TAG_BIO_READ_DESC)
public class BioController
{
    private final BioReadService bioReadService;
    @GetMapping("/pages/{username}") @Operation(summary = ControllerConstants.BIO_GET_PAGE_SUMMARY, description = ControllerConstants.BIO_GET_PAGE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_PAGE_RETRIEVED, content = @Content(schema = @Schema(implementation = BioPageResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
    public ResponseEntity<BioPageResponse> getBioPage( @Parameter(description = ControllerConstants.PARAM_USERNAME) @PathVariable String username )
    {
        return ResponseEntity.ok( bioReadService.getBioPageByUsername( username ) );
    }

    @GetMapping("/pages/owner/{ownerId}") @Operation(summary = ControllerConstants.BIO_GET_PAGES_BY_OWNER_SUMMARY, description = ControllerConstants.BIO_GET_PAGES_BY_OWNER_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_PAGES_RETRIEVED, content = @Content(schema = @Schema(implementation = BioPageResponse.class))) })
    public ResponseEntity<List<BioPageResponse>> getBioPagesByOwner( @Parameter(description = ControllerConstants.PARAM_OWNER_ID) @PathVariable String ownerId )
    {
        return ResponseEntity.ok( bioReadService.getBioPagesByOwner( ownerId ) );
    }

    @GetMapping("/links/{id}") @Operation(summary = ControllerConstants.BIO_GET_LINK_SUMMARY, description = ControllerConstants.BIO_GET_LINK_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_LINK_RETRIEVED, content = @Content(schema = @Schema(implementation = BioLinkResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_LINK_NOT_FOUND) })
    public ResponseEntity<BioLinkResponse> getBioLinkById( @Parameter(description = ControllerConstants.PARAM_BIO_LINK_ID) @PathVariable Long id )
    {
        return ResponseEntity.ok( bioReadService.getBioLinkById( id ) );
    }

    @GetMapping("/links/page/{pageId}") @Operation(summary = ControllerConstants.BIO_GET_LINKS_BY_PAGE_SUMMARY, description = ControllerConstants.BIO_GET_LINKS_BY_PAGE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_LINKS_RETRIEVED, content = @Content(schema = @Schema(implementation = BioLinkResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
    public ResponseEntity<List<BioLinkResponse>> getBioLinksByPageId( @Parameter(description = ControllerConstants.PARAM_BIO_PAGE_ID) @PathVariable Long pageId )
    {
        return ResponseEntity.ok( bioReadService.getBioLinksByPageId( pageId ) );
    }

    @GetMapping("/links/page/{pageId}/active") @Operation(summary = ControllerConstants.BIO_GET_ACTIVE_LINKS_SUMMARY, description = ControllerConstants.BIO_GET_ACTIVE_LINKS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_LINKS_RETRIEVED, content = @Content(schema = @Schema(implementation = BioLinkResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND) })
    public ResponseEntity<List<BioLinkResponse>> getActiveBioLinksByPageId( @Parameter(description = ControllerConstants.PARAM_BIO_PAGE_ID) @PathVariable Long pageId )
    {
        return ResponseEntity.ok( bioReadService.getActiveBioLinksByPageId( pageId ) );
    }
}
