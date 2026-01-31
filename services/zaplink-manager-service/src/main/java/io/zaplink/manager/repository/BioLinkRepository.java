package io.zaplink.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.entity.BioLinkEntity;

@Repository
public interface BioLinkRepository extends JpaRepository<BioLinkEntity, Long> {
    
    List<BioLinkEntity> findByBioPageIdOrderBySortOrderAsc(Long pageId);
    
    List<BioLinkEntity> findByBioPageIdAndIsActiveOrderBySortOrderAsc(Long pageId, Boolean isActive);
    
}
