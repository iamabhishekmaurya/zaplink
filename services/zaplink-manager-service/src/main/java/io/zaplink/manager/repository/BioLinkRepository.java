package io.zaplink.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.entity.BioLinkEntity;

@Repository
public interface BioLinkRepository
    extends
    JpaRepository<BioLinkEntity, Long>
{
    List<BioLinkEntity> findByBioPageIdOrderBySortOrderAsc( Long pageId );

    // We keep this for backward compatibility or simple use cases
    List<BioLinkEntity> findByBioPageIdAndIsActiveOrderBySortOrderAsc( Long pageId, Boolean isActive );

    @org.springframework.data.jpa.repository.Query("SELECT l FROM BioLinkEntity l WHERE l.bioPage.id = :pageId AND l.isActive = true "
            + "AND (l.scheduleFrom IS NULL OR l.scheduleFrom <= :currentTime) "
            + "AND (l.scheduleTo IS NULL OR l.scheduleTo >= :currentTime) " + "ORDER BY l.sortOrder ASC")
    List<BioLinkEntity> findActiveLinksByPageId( @org.springframework.data.repository.query.Param("pageId") Long pageId,
                                                 @org.springframework.data.repository.query.Param("currentTime") java.time.LocalDateTime currentTime );
}
