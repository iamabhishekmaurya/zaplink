package io.zaplink.media.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.media.common.constants.ControllerConstants;
import io.zaplink.media.common.constants.LogConstants;
import io.zaplink.media.common.constants.StatusConstants;
import io.zaplink.media.entity.Asset;
import io.zaplink.media.repository.AssetRepository;
import io.zaplink.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for Media Management using the "media" resource.
 * Provides endpoints for uploading, listing, and deleting assets.
 * 
 * Versioning: Enforced via "X-API-VERSION=1" header.
 * Base Path: /api/media (Mapped via Gateway or direct)
 */
@RestController @RequestMapping(value = "/media") @RequiredArgsConstructor @Slf4j @Tag(name = ControllerConstants.TAG_MEDIA_MANAGEMENT, description = ControllerConstants.TAG_MEDIA_MANAGEMENT_DESC)
public class MediaController
{
    private final MediaService    mediaService;
    private final AssetRepository assetRepository;
    /**
     * Uploads a media file.
     */
    @PostMapping("/upload") @Operation(summary = ControllerConstants.MEDIA_UPLOAD_SUMMARY, description = ControllerConstants.MEDIA_UPLOAD_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_ASSET_UPLOADED, content = @Content(schema = @Schema(implementation = Asset.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_FILE),
      @ApiResponse(responseCode = StatusConstants.STATUS_500_INTERNAL_SERVER_ERROR, description = ControllerConstants.RESPONSE_500_STORAGE_ERROR) })
    public ResponseEntity<Asset> uploadMedia( @Parameter(description = ControllerConstants.PARAM_FILE) @RequestParam("file") MultipartFile file,
                                              @Parameter(description = ControllerConstants.PARAM_OWNER_ID) @RequestParam("ownerId") String ownerId,
                                              @Parameter(description = ControllerConstants.PARAM_FOLDER_ID) @RequestParam(value = "folderId", required = false) UUID folderId )
    {
        log.info( LogConstants.LOG_CONTROLLER_UPLOAD_REQ, ownerId, folderId, file.getOriginalFilename() );
        Asset asset = mediaService.uploadAsset( file, ownerId, folderId );
        log.info( LogConstants.LOG_CONTROLLER_UPLOAD_SUCCESS, asset.getId() );
        return ResponseEntity.ok( asset );
    }

    /**
     * Lists assets with optional filtering.
     */
    @GetMapping @Operation(summary = ControllerConstants.MEDIA_LIST_SUMMARY, description = ControllerConstants.MEDIA_LIST_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_ASSETS_LISTED) })
    public ResponseEntity<Page<Asset>> listAssets( @Parameter(description = ControllerConstants.PARAM_FOLDER_ID) @RequestParam(value = "folderId", required = false) UUID folderId,
                                                   @Parameter(description = ControllerConstants.PARAM_OWNER_ID) @RequestParam(value = "ownerId", required = false) String ownerId,
                                                   @PageableDefault(size = 20) Pageable pageable )
    {
        log.info( LogConstants.LOG_CONTROLLER_LIST_REQ, ownerId, folderId, pageable.getPageNumber() );
        if ( folderId != null )
        {
            return ResponseEntity.ok( assetRepository.findByFolderId( folderId, pageable ) );
        }
        if ( ownerId != null )
        {
            return ResponseEntity.ok( assetRepository.findByOwnerId( ownerId, pageable ) );
        }
        return ResponseEntity.ok( assetRepository.findAll( pageable ) );
    }

    /**
     * Deletes an asset by ID.
     */
    @DeleteMapping("/{id}") @Operation(summary = ControllerConstants.MEDIA_DELETE_SUMMARY, description = ControllerConstants.MEDIA_DELETE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_204_NO_CONTENT, description = ControllerConstants.RESPONSE_204_ASSET_DELETED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_ASSET_NOT_FOUND) })
    public ResponseEntity<Void> deleteAsset( @Parameter(description = ControllerConstants.PARAM_ASSET_ID) @PathVariable("id") UUID id )
    {
        log.info( LogConstants.LOG_CONTROLLER_DELETE_REQ, id );
        mediaService.deleteAsset( id );
        return ResponseEntity.noContent().build();
    }
}