package io.zaplink.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.BioLinkEntity;

@Repository
public interface BioLinkRepository
    extends
    JpaRepository<BioLinkEntity, Long>
{
    List<BioLinkEntity> findAllByPageIdOrderBySortOrderAsc( Long pageId );

    void deleteAllByPageId( Long pageId );
}
