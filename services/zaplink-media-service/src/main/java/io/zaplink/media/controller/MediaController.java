package io.zaplink.media.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.zaplink.media.entity.MediaItem;
import io.zaplink.media.service.MediaItemService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/medias") @RequiredArgsConstructor
public class MediaController
{
    private final MediaItemService mediaItemService;
    @PostMapping
    public ResponseEntity<MediaItem> uploadMedia( @RequestHeader("X-User-Id") String ownerId,
                                                  @RequestParam("file") MultipartFile file,
                                                  @RequestParam(value = "folderId", required = false) UUID folderId,
                                                  @RequestParam(value = "tags", required = false) List<String> tags )
    {
        return ResponseEntity.ok( mediaItemService.uploadMedia( file, folderId, ownerId, tags ) );
    }

    @GetMapping
    public ResponseEntity<List<MediaItem>> listMedia( @RequestHeader("X-User-Id") String ownerId,
                                                      @RequestParam(value = "folderId", required = false) UUID folderId )
    {
        return ResponseEntity.ok( mediaItemService.listMedia( ownerId, folderId ) );
    }

    @GetMapping("/all")
    public ResponseEntity<List<MediaItem>> listAllMedia( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( mediaItemService.listAllMedia( ownerId ) );
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<MediaItem> updateMetadata( @RequestHeader("X-User-Id") String ownerId,
                                                     @PathVariable("id") UUID id,
                                                     @RequestParam(value = "newName", required = false) String newName,
                                                     @RequestParam(value = "newTags", required = false) List<String> newTags )
    {
        return ResponseEntity.ok( mediaItemService.updateMetadata( id, newName, newTags, ownerId ) );
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<MediaItem> moveMedia( @RequestHeader("X-User-Id") String ownerId,
                                                @PathVariable("id") UUID id,
                                                @RequestParam(value = "newFolderId", required = false) UUID newFolderId )
    {
        return ResponseEntity.ok( mediaItemService.moveMedia( id, newFolderId, ownerId ) );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia( @RequestHeader("X-User-Id") String ownerId, @PathVariable("id") UUID id )
    {
        mediaItemService.deleteMedia( id, ownerId );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteMedia( @RequestHeader("X-User-Id") String ownerId,
                                                 @PathVariable("id") UUID id )
    {
        mediaItemService.hardDeleteTrashedMedia( id, ownerId );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<MediaItem> restoreMedia( @RequestHeader("X-User-Id") String ownerId,
                                                   @PathVariable("id") UUID id )
    {
        return ResponseEntity.ok( mediaItemService.restoreMedia( id, ownerId ) );
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<MediaItem> toggleMediaFavorite( @RequestHeader("X-User-Id") String ownerId,
                                                          @PathVariable("id") UUID id )
    {
        return ResponseEntity.ok( mediaItemService.toggleFavorite( id, ownerId ) );
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<MediaItem>> listFavoriteMedia( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( mediaItemService.listFavorites( ownerId ) );
    }

    @GetMapping("/trash")
    public ResponseEntity<List<MediaItem>> listTrashMedia( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( mediaItemService.listTrash( ownerId ) );
    }
}
