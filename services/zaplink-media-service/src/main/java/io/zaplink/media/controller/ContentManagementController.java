package io.zaplink.media.controller;

import io.zaplink.media.entity.Folder;
import io.zaplink.media.entity.MediaItem;
import io.zaplink.media.service.FolderService;
import io.zaplink.media.service.MediaItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/content") @RequiredArgsConstructor
public class ContentManagementController
{
    private final FolderService    folderService;
    private final MediaItemService mediaItemService;
    // We assume the ownerId is retrieved from the auth token via a header or context
    // For simplicity, we use a request header "X-User-Id" which api-gateway passes
    // --- FOLDER ENDPOINTS ---
    @PostMapping("/folders")
    public ResponseEntity<Folder> createFolder( @RequestHeader("X-User-Id") String ownerId,
                                                @RequestParam String name,
                                                @RequestParam(required = false) UUID parentId )
    {
        return ResponseEntity.ok( folderService.createFolder( name, parentId, ownerId ) );
    }

    @GetMapping("/folders")
    public ResponseEntity<List<Folder>> listFolders( @RequestHeader("X-User-Id") String ownerId,
                                                     @RequestParam(required = false) UUID parentId )
    {
        return ResponseEntity.ok( folderService.listFolders( ownerId, parentId ) );
    }

    @PutMapping("/folders/{id}/rename")
    public ResponseEntity<Folder> renameFolder( @RequestHeader("X-User-Id") String ownerId,
                                                @PathVariable UUID id,
                                                @RequestParam String newName )
    {
        return ResponseEntity.ok( folderService.renameFolder( id, newName, ownerId ) );
    }

    @PutMapping("/folders/{id}/move")
    public ResponseEntity<Folder> moveFolder( @RequestHeader("X-User-Id") String ownerId,
                                              @PathVariable UUID id,
                                              @RequestParam(required = false) UUID newParentId )
    {
        return ResponseEntity.ok( folderService.moveFolder( id, newParentId, ownerId ) );
    }

    @DeleteMapping("/folders/{id}")
    public ResponseEntity<Void> deleteFolder( @RequestHeader("X-User-Id") String ownerId, @PathVariable UUID id )
    {
        folderService.deleteFolder( id, ownerId );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/folders/{id}/restore")
    public ResponseEntity<Folder> restoreFolder( @RequestHeader("X-User-Id") String ownerId, @PathVariable UUID id )
    {
        return ResponseEntity.ok( folderService.restoreFolder( id, ownerId ) );
    }

    @PostMapping("/folders/{id}/favorite")
    public ResponseEntity<Folder> toggleFolderFavorite( @RequestHeader("X-User-Id") String ownerId,
                                                        @PathVariable UUID id )
    {
        return ResponseEntity.ok( folderService.toggleFavorite( id, ownerId ) );
    }
    // --- MEDIA EXNDPOINTS ---

    @PostMapping("/media")
    public ResponseEntity<MediaItem> uploadMedia( @RequestHeader("X-User-Id") String ownerId,
                                                  @RequestParam("file") MultipartFile file,
                                                  @RequestParam(required = false) UUID folderId,
                                                  @RequestParam(required = false) List<String> tags )
    {
        return ResponseEntity.ok( mediaItemService.uploadMedia( file, folderId, ownerId, tags ) );
    }

    @GetMapping("/media")
    public ResponseEntity<List<MediaItem>> listMedia( @RequestHeader("X-User-Id") String ownerId,
                                                      @RequestParam(required = false) UUID folderId )
    {
        return ResponseEntity.ok( mediaItemService.listMedia( ownerId, folderId ) );
    }

    @PutMapping("/media/{id}/metadata")
    public ResponseEntity<MediaItem> updateMetadata( @RequestHeader("X-User-Id") String ownerId,
                                                     @PathVariable UUID id,
                                                     @RequestParam(required = false) String newName,
                                                     @RequestParam(required = false) List<String> newTags )
    {
        return ResponseEntity.ok( mediaItemService.updateMetadata( id, newName, newTags, ownerId ) );
    }

    @PutMapping("/media/{id}/move")
    public ResponseEntity<MediaItem> moveMedia( @RequestHeader("X-User-Id") String ownerId,
                                                @PathVariable UUID id,
                                                @RequestParam(required = false) UUID newFolderId )
    {
        return ResponseEntity.ok( mediaItemService.moveMedia( id, newFolderId, ownerId ) );
    }

    @DeleteMapping("/media/{id}")
    public ResponseEntity<Void> deleteMedia( @RequestHeader("X-User-Id") String ownerId, @PathVariable UUID id )
    {
        mediaItemService.deleteMedia( id, ownerId );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/media/{id}/restore")
    public ResponseEntity<MediaItem> restoreMedia( @RequestHeader("X-User-Id") String ownerId, @PathVariable UUID id )
    {
        return ResponseEntity.ok( mediaItemService.restoreMedia( id, ownerId ) );
    }

    @PostMapping("/media/{id}/favorite")
    public ResponseEntity<MediaItem> toggleMediaFavorite( @RequestHeader("X-User-Id") String ownerId,
                                                          @PathVariable UUID id )
    {
        return ResponseEntity.ok( mediaItemService.toggleFavorite( id, ownerId ) );
    }
    // --- GLOBAL VIEWS (Favorites, Trash) ---

    @GetMapping("/favorites/folders")
    public ResponseEntity<List<Folder>> listFavoriteFolders( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( folderService.listFavorites( ownerId ) );
    }

    @GetMapping("/favorites/media")
    public ResponseEntity<List<MediaItem>> listFavoriteMedia( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( mediaItemService.listFavorites( ownerId ) );
    }

    @GetMapping("/trash/folders")
    public ResponseEntity<List<Folder>> listTrashFolders( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( folderService.listTrash( ownerId ) );
    }

    @GetMapping("/trash/media")
    public ResponseEntity<List<MediaItem>> listTrashMedia( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( mediaItemService.listTrash( ownerId ) );
    }
}
