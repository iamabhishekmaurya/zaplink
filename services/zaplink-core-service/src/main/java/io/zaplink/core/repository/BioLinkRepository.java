package io.zaplink.core.repository;

import io.zaplink.core.entity.BioLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BioLinkRepository
    extends
    JpaRepository<BioLinkEntity, Long>
{
    List<BioLinkEntity> findAllByPageIdOrderBySortOrderAsc( Long pageId );

    void deleteAllByPageId( Long pageId );
}
