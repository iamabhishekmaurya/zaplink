package io.zaplink.media.repository;

import io.zaplink.media.entity.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaItemRepository
    extends
    JpaRepository<MediaItem, UUID>
{
    List<MediaItem> findByOwnerIdAndFolderIdAndIsDeletedFalse( String ownerId, UUID folderId );

    List<MediaItem> findByOwnerIdAndFolderIsNullAndIsDeletedFalse( String ownerId );

    List<MediaItem> findByOwnerIdAndIsDeletedTrue( String ownerId );

    List<MediaItem> findByOwnerIdAndIsFavoriteTrueAndIsDeletedFalse( String ownerId );

    Optional<MediaItem> findByIdAndOwnerIdAndIsDeletedFalse( UUID id, String ownerId );

    Optional<MediaItem> findByIdAndOwnerId( UUID id, String ownerId );
}
