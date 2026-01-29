package io.zaplink.media.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.media.common.constants.ExceptionConstants;
import io.zaplink.media.common.constants.LogConstants;
import io.zaplink.media.common.exception.AssetNotFoundException;
import io.zaplink.media.entity.Folder;
import io.zaplink.media.repository.AssetRepository;
import io.zaplink.media.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing media folders.
 * Handles hierarchy and simple CRUD.
 */
@Service @RequiredArgsConstructor @Slf4j
public class FolderService
{
    private final FolderRepository folderRepository;
    private final AssetRepository  assetRepository;
    /**
     * Creates a new folder.
     * 
     * @param name The name of the folder.
     * @param parentId The optional parent folder ID.
     * @param ownerId The ID of the owner.
     * @return The created Folder.
     */
    @Transactional
    public Folder createFolder( String name, UUID parentId, String ownerId )
    {
        log.info( LogConstants.LOG_FOLDER_CREATE_REQ, name, parentId, ownerId );
        Folder parent = null;
        if ( parentId != null )
        {
            parent = folderRepository.findById( parentId )
                    .orElseThrow( () -> new AssetNotFoundException( ExceptionConstants.ERR_PARENT_FOLDER_NOT_FOUND
                            + parentId ) );
        }
        Folder folder = Folder.builder().name( name ).parent( parent ).ownerId( ownerId ).build();
        folder = folderRepository.save( folder );
        log.info( LogConstants.LOG_FOLDER_CREATED, folder.getId() );
        return folder;
    }

    /**
     * Lists folders for a specific owner, optionally within a parent folder.
     * 
     * @param ownerId The owner ID.
     * @param parentId The parent folder ID (can be null for root).
     * @return List of folders.
     */
    public List<Folder> listFolders( String ownerId, UUID parentId )
    {
        log.info( LogConstants.LOG_FOLDER_LIST_REQ, ownerId, parentId );
        return folderRepository.findByOwnerIdAndParentId( ownerId, parentId );
    }

    /**
     * Deletes a folder if it is empty.
     * 
     * @param folderId The ID of the folder to delete.
     * @throws IllegalArgumentException if the folder is not empty.
     */
    @Transactional
    public void deleteFolder( UUID folderId )
    {
        log.info( LogConstants.LOG_FOLDER_DELETE_REQ, folderId );
        if ( !folderRepository.existsById( folderId ) )
        {
            throw new AssetNotFoundException( ExceptionConstants.ERR_FOLDER_NOT_FOUND + folderId );
        }
        boolean hasSubfolders = folderRepository.existsByParentId( folderId );
        boolean hasAssets = assetRepository.existsByFolderId( folderId );
        if ( hasSubfolders || hasAssets )
        {
            log.warn( LogConstants.LOG_FOLDER_NOT_EMPTY, folderId,
                      ( hasSubfolders ? "subfolders" : "" ) + ( hasAssets ? " assets" : "" ) );
            throw new IllegalArgumentException( ExceptionConstants.ERR_FOLDER_NOT_EMPTY );
        }
        folderRepository.deleteById( folderId );
        log.info( LogConstants.LOG_FOLDER_DELETED, folderId );
    }
}
