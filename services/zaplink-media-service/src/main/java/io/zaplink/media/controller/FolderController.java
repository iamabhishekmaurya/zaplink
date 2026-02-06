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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.media.common.constants.AppConstants;
import io.zaplink.media.common.constants.ControllerConstants;
import io.zaplink.media.common.constants.StatusConstants;
import io.zaplink.media.dto.request.CreateFolderRequest;
import io.zaplink.media.entity.Folder;
import io.zaplink.media.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping(value = AppConstants.BASE_PATH_FOLDERS) @RequiredArgsConstructor @Tag(name = ControllerConstants.TAG_FOLDER_MANAGEMENT, description = ControllerConstants.TAG_FOLDER_MANAGEMENT_DESC)
public class FolderController
{
    private final FolderService folderService;
    @PostMapping @Operation(summary = ControllerConstants.FOLDER_CREATE_SUMMARY, description = ControllerConstants.FOLDER_CREATE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_FOLDER_CREATED, content = @Content(schema = @Schema(implementation = Folder.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA) })
    public ResponseEntity<Folder> createFolder( @Valid @RequestBody CreateFolderRequest request )
    {
        return ResponseEntity.ok( folderService.createFolder( request.name(), request.parentId(), request.ownerId() ) );
    }

    @GetMapping @Operation(summary = ControllerConstants.FOLDER_LIST_SUMMARY, description = ControllerConstants.FOLDER_LIST_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_FOLDERS_LISTED) })
    public ResponseEntity<List<Folder>> listFolders( @Parameter(description = ControllerConstants.PARAM_OWNER_ID) @RequestParam(value = "ownerId") String ownerId,
                                                     @Parameter(description = ControllerConstants.PARAM_PARENT_ID) @RequestParam(value = "parentId", required = false) UUID parentId )
    {
        return ResponseEntity.ok( folderService.listFolders( ownerId, parentId ) );
    }

    @DeleteMapping("/{id}") @Operation(summary = ControllerConstants.FOLDER_DELETE_SUMMARY, description = ControllerConstants.FOLDER_DELETE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_204_NO_CONTENT, description = ControllerConstants.RESPONSE_204_FOLDER_DELETED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_FOLDER_NOT_FOUND) })
    public ResponseEntity<Void> deleteFolder( @Parameter(description = ControllerConstants.PARAM_FOLDER_ID) @PathVariable("id") UUID id )
    {
        folderService.deleteFolder( id );
        return ResponseEntity.noContent().build();
    }
}
