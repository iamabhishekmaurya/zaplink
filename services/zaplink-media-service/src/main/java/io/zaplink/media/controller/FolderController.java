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

import io.zaplink.media.entity.Folder;
import io.zaplink.media.service.FolderService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/folders") @RequiredArgsConstructor
public class FolderController
{
    private final FolderService folderService;
    @PostMapping
    public ResponseEntity<Folder> createFolder( @RequestHeader("X-User-Id") String ownerId,
                                                @RequestParam("name") String name,
                                                @RequestParam(value = "parentId", required = false) UUID parentId )
    {
        return ResponseEntity.ok( folderService.createFolder( name, parentId, ownerId ) );
    }

    @GetMapping
    public ResponseEntity<List<Folder>> listFolders( @RequestHeader("X-User-Id") String ownerId,
                                                     @RequestParam(value = "parentId", required = false) UUID parentId )
    {
        return ResponseEntity.ok( folderService.listFolders( ownerId, parentId ) );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Folder>> listAllFolders( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( folderService.listAllFolders( ownerId ) );
    }

    @PutMapping("/{id}/rename")
    public ResponseEntity<Folder> renameFolder( @RequestHeader("X-User-Id") String ownerId,
                                                @PathVariable("id") UUID id,
                                                @RequestParam("newName") String newName )
    {
        return ResponseEntity.ok( folderService.renameFolder( id, newName, ownerId ) );
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<Folder> moveFolder( @RequestHeader("X-User-Id") String ownerId,
                                              @PathVariable("id") UUID id,
                                              @RequestParam(value = "newParentId", required = false) UUID newParentId )
    {
        return ResponseEntity.ok( folderService.moveFolder( id, newParentId, ownerId ) );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder( @RequestHeader("X-User-Id") String ownerId, @PathVariable("id") UUID id )
    {
        folderService.deleteFolder( id, ownerId );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteFolder( @RequestHeader("X-User-Id") String ownerId,
                                                  @PathVariable("id") UUID id )
    {
        folderService.hardDeleteTrashedFolder( id, ownerId );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Folder> restoreFolder( @RequestHeader("X-User-Id") String ownerId,
                                                 @PathVariable("id") UUID id )
    {
        return ResponseEntity.ok( folderService.restoreFolder( id, ownerId ) );
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Folder> toggleFolderFavorite( @RequestHeader("X-User-Id") String ownerId,
                                                        @PathVariable("id") UUID id )
    {
        return ResponseEntity.ok( folderService.toggleFavorite( id, ownerId ) );
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Folder>> listFavoriteFolders( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( folderService.listFavorites( ownerId ) );
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Folder>> listTrashFolders( @RequestHeader("X-User-Id") String ownerId )
    {
        return ResponseEntity.ok( folderService.listTrash( ownerId ) );
    }
}
