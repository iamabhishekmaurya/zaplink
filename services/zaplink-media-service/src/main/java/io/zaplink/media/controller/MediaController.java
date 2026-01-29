package io.zaplink.media.controller;

import io.zaplink.media.common.constants.AppConstants;
import io.zaplink.media.common.constants.LogConstants;
import io.zaplink.media.entity.Asset;
import io.zaplink.media.repository.AssetRepository;
import io.zaplink.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * REST Controller for Media Management using the "media" resource.
 * Provides endpoints for uploading, listing, and deleting assets.
 * 
 * Versioning: Enforced via "X-API-VERSION=1" header.
 * Base Path: /api/media (Mapped via Gateway or direct)
 */
@RestController @RequestMapping(value = "/media") @RequiredArgsConstructor @Slf4j
public class MediaController
{
    private final MediaService    mediaService;
    private final AssetRepository assetRepository;
    /**
     * Uploads a media file.
     * 
     * @param file The file binary.
     * @param ownerId The ID of the authenticated user.
     * @param folderId Optional ID of the folder to place the file in.
     * @return The created Asset object.
     */
    @PostMapping("/upload")
    public ResponseEntity<Asset> uploadMedia( @RequestParam("file") MultipartFile file,
                                              @RequestParam("ownerId") UUID ownerId,
                                              @RequestParam(value = "folderId", required = false) UUID folderId )
    {
        log.info( LogConstants.LOG_CONTROLLER_UPLOAD_REQ, ownerId, folderId, file.getOriginalFilename() );
        Asset asset = mediaService.uploadAsset( file, ownerId, folderId );
        log.info( LogConstants.LOG_CONTROLLER_UPLOAD_SUCCESS, asset.getId() );
        return ResponseEntity.ok( asset );
    }

    /**
     * Lists assets with optional filtering.
     * 
     * @param folderId Filter by folder.
     * @param ownerId Filter by owner.
     * @param pageable Pagination info (page, size, sort).
     * @return A page of Asset entities.
     */
    @GetMapping
    public ResponseEntity<Page<Asset>> listAssets( @RequestParam(value = "folderId", required = false) UUID folderId,
                                                   @RequestParam(value = "ownerId", required = false) UUID ownerId,
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
     * Performs a permanent delete of both the database record and the file in storage.
     * 
     * @param id The UUID of the asset to delete.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset( @PathVariable UUID id )
    {
        log.info( LogConstants.LOG_CONTROLLER_DELETE_REQ, id );
        mediaService.deleteAsset( id );
        return ResponseEntity.noContent().build();
    }
}