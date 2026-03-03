package io.zaplink.media.service;

import io.zaplink.media.entity.Folder;
import io.zaplink.media.repository.FolderRepository;
import io.zaplink.media.repository.MediaItemRepository;
import io.zaplink.media.common.exception.AssetNotFoundException;
import io.zaplink.media.common.constants.ExceptionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class FolderService
{
    private final FolderRepository    folderRepository;
    private final MediaItemRepository mediaItemRepository;
    @Transactional
    public Folder createFolder( String name, UUID parentId, String ownerId )
    {
        Folder parent = null;
        if ( parentId != null )
        {
            parent = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( parentId, ownerId )
                    .orElseThrow( () -> new AssetNotFoundException( "Parent folder not found" ) );
        }
        Folder folder = Folder.builder().name( name ).parent( parent ).ownerId( ownerId ).build();
        return folderRepository.save( folder );
    }

    @Transactional
    public Folder renameFolder( UUID folderId, String newName, String ownerId )
    {
        Folder folder = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( folderId, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        folder.setName( newName );
        return folderRepository.save( folder );
    }

    @Transactional
    public Folder moveFolder( UUID folderId, UUID newParentId, String ownerId )
    {
        Folder folder = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( folderId, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        Folder parent = null;
        if ( newParentId != null )
        {
            parent = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( newParentId, ownerId )
                    .orElseThrow( () -> new AssetNotFoundException( "Parent folder not found" ) );
        }
        folder.setParent( parent );
        return folderRepository.save( folder );
    }

    public List<Folder> listFolders( String ownerId, UUID parentId )
    {
        if ( parentId == null )
        {
            return folderRepository.findByOwnerIdAndParentIsNullAndIsDeletedFalse( ownerId );
        }
        return folderRepository.findByOwnerIdAndParentIdAndIsDeletedFalse( ownerId, parentId );
    }

    public List<Folder> listAllFolders( String ownerId )
    {
        return folderRepository.findByOwnerIdAndIsDeletedFalse( ownerId );
    }

    @Transactional
    public void deleteFolder( UUID folderId, String ownerId )
    {
        Folder folder = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( folderId, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        folder.setDeleted( true );
        folderRepository.save( folder );
        // Cascading soft delete to subfolders and media items can be handled async or here.
        // For simplicity, we just mark this folder. Its contents won't be visible since parent is deleted.
    }

    @Transactional
    public Folder restoreFolder( UUID folderId, String ownerId )
    {
        Folder folder = folderRepository.findByIdAndOwnerId( folderId, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        folder.setDeleted( false );
        return folderRepository.save( folder );
    }

    @Transactional
    public Folder toggleFavorite( UUID folderId, String ownerId )
    {
        Folder folder = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( folderId, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        folder.setFavorite( !folder.isFavorite() );
        return folderRepository.save( folder );
    }

    public List<Folder> listFavorites( String ownerId )
    {
        return folderRepository.findByOwnerIdAndIsFavoriteTrueAndIsDeletedFalse( ownerId );
    }

    public List<Folder> listTrash( String ownerId )
    {
        return folderRepository.findByOwnerIdAndIsDeletedTrue( ownerId );
    }

    @Transactional
    public void hardDeleteTrashedFolder( UUID folderId, String ownerId )
    {
        Folder folder = folderRepository.findByIdAndOwnerId( folderId, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        if ( !folder.isDeleted() )
        {
            throw new IllegalStateException( "Only trashed items can be permanently deleted" );
        }
        folderRepository.delete( folder );
    }
}
