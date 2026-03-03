package io.zaplink.media.service;

import io.zaplink.media.entity.Folder;
import io.zaplink.media.entity.MediaItem;
import io.zaplink.media.repository.FolderRepository;
import io.zaplink.media.repository.MediaItemRepository;
import io.zaplink.media.common.exception.AssetNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class MediaItemService
{
    private final MediaItemRepository mediaItemRepository;
    private final FolderRepository    folderRepository;
    private final MinioStorageService storageService;     // Reuse existing S3 integration
    @Transactional
    public MediaItem uploadMedia( MultipartFile file, UUID folderId, String ownerId, List<String> tags )
    {
        Folder folder = null;
        if ( folderId != null )
        {
            folder = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( folderId, ownerId )
                    .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        }
        // Upload to storage
        String key = ownerId + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String url;
        try
        {
            url = storageService.upload( file, key );
        }
        catch ( java.io.IOException e )
        {
            throw new RuntimeException( "Failed to upload file", e );
        }
        MediaItem mediaItem = MediaItem.builder().ownerId( ownerId ).folder( folder ).name( file.getOriginalFilename() )
                .type( file.getContentType() ).size( file.getSize() ).url( url ).tags( tags ).build();
        return mediaItemRepository.save( mediaItem );
    }

    @Transactional
    public MediaItem updateMetadata( UUID id, String newName, List<String> newTags, String ownerId )
    {
        MediaItem item = mediaItemRepository.findByIdAndOwnerIdAndIsDeletedFalse( id, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Media not found" ) );
        if ( newName != null )
            item.setName( newName );
        if ( newTags != null )
            item.setTags( newTags );
        return mediaItemRepository.save( item );
    }

    @Transactional
    public MediaItem moveMedia( UUID id, UUID newFolderId, String ownerId )
    {
        MediaItem item = mediaItemRepository.findByIdAndOwnerIdAndIsDeletedFalse( id, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Media not found" ) );
        Folder folder = null;
        if ( newFolderId != null )
        {
            folder = folderRepository.findByIdAndOwnerIdAndIsDeletedFalse( newFolderId, ownerId )
                    .orElseThrow( () -> new AssetNotFoundException( "Folder not found" ) );
        }
        item.setFolder( folder );
        return mediaItemRepository.save( item );
    }

    @Transactional
    public void deleteMedia( UUID id, String ownerId )
    {
        MediaItem item = mediaItemRepository.findByIdAndOwnerIdAndIsDeletedFalse( id, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Media not found" ) );
        item.setDeleted( true );
        mediaItemRepository.save( item );
    }

    @Transactional
    public MediaItem restoreMedia( UUID id, String ownerId )
    {
        MediaItem item = mediaItemRepository.findByIdAndOwnerId( id, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Media not found" ) );
        item.setDeleted( false );
        return mediaItemRepository.save( item );
    }

    @Transactional
    public MediaItem toggleFavorite( UUID id, String ownerId )
    {
        MediaItem item = mediaItemRepository.findByIdAndOwnerIdAndIsDeletedFalse( id, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Media not found" ) );
        item.setFavorite( !item.isFavorite() );
        return mediaItemRepository.save( item );
    }

    public List<MediaItem> listAllMedia( String ownerId )
    {
        return mediaItemRepository.findByOwnerIdAndIsDeletedFalse( ownerId );
    }

    public List<MediaItem> listMedia( String ownerId, UUID folderId )
    {
        if ( folderId == null )
        {
            return mediaItemRepository.findByOwnerIdAndFolderIsNullAndIsDeletedFalse( ownerId );
        }
        return mediaItemRepository.findByOwnerIdAndFolderIdAndIsDeletedFalse( ownerId, folderId );
    }

    public List<MediaItem> listFavorites( String ownerId )
    {
        return mediaItemRepository.findByOwnerIdAndIsFavoriteTrueAndIsDeletedFalse( ownerId );
    }

    public List<MediaItem> listTrash( String ownerId )
    {
        return mediaItemRepository.findByOwnerIdAndIsDeletedTrue( ownerId );
    }

    @Transactional
    public void hardDeleteTrashedMedia( UUID id, String ownerId )
    {
        MediaItem item = mediaItemRepository.findByIdAndOwnerId( id, ownerId )
                .orElseThrow( () -> new AssetNotFoundException( "Media not found" ) );
        if ( !item.isDeleted() )
        {
            throw new IllegalStateException( "Only trashed items can be permanently deleted" );
        }
        mediaItemRepository.delete( item );
        // Delete from S3 via MinioStorageService
    }
}
