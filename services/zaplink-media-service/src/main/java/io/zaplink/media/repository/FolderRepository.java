package io.zaplink.media.repository;

import io.zaplink.media.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository
    extends
    JpaRepository<Folder, UUID>
{
    List<Folder> findByParentIdAndIsDeletedFalse( UUID parentId );

    List<Folder> findByOwnerIdAndIsDeletedFalse( String ownerId );

    List<Folder> findByOwnerIdAndParentIsNullAndIsDeletedFalse( String ownerId );

    List<Folder> findByOwnerIdAndParentIdAndIsDeletedFalse( String ownerId, UUID parentId );

    List<Folder> findByOwnerIdAndIsDeletedTrue( String ownerId );

    List<Folder> findByOwnerIdAndIsFavoriteTrueAndIsDeletedFalse( String ownerId );

    Optional<Folder> findByIdAndOwnerIdAndIsDeletedFalse( UUID id, String ownerId );

    Optional<Folder> findByIdAndOwnerId( UUID id, String ownerId );

    boolean existsByParentIdAndIsDeletedFalse( UUID parentId );
}
