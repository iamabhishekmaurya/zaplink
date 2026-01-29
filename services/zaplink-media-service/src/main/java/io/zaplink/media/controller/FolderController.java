package io.zaplink.media.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.media.common.constants.AppConstants;
import io.zaplink.media.dto.request.CreateFolderRequest;
import io.zaplink.media.entity.Folder;
import io.zaplink.media.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping(value = AppConstants.BASE_PATH_MEDIA + "/folders") @RequiredArgsConstructor
public class FolderController
{
    private final FolderService folderService;
    @PostMapping
    public ResponseEntity<Folder> createFolder( @Valid @RequestBody CreateFolderRequest request )
    {
        return ResponseEntity
                .ok( folderService.createFolder( request.getName(), request.getParentId(), request.getOwnerId() ) );
    }

    @GetMapping
    public ResponseEntity<List<Folder>> listFolders( @RequestParam(value = "ownerId") String ownerId,
                                                     @RequestParam(value = "parentId", required = false) UUID parentId )
    {
        return ResponseEntity.ok( folderService.listFolders( ownerId, parentId ) );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder( @PathVariable("id") UUID id )
    {
        folderService.deleteFolder( id );
        return ResponseEntity.noContent().build();
    }
}
