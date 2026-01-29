package io.zaplink.media.repository;

import io.zaplink.media.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetRepository
    extends
    JpaRepository<Asset, UUID>
{
    Page<Asset> findByFolderId( UUID folderId, Pageable pageable );

    Page<Asset> findByOwnerId( UUID ownerId, Pageable pageable );
}
