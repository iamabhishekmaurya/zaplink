package io.zaplink.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.entity.RedirectRuleEntity;

@Repository
public interface RedirectRuleRepository
    extends
    JpaRepository<RedirectRuleEntity, Long>
{
    List<RedirectRuleEntity> findByUrlMappingIdOrderByPriorityDesc( Long urlMappingId );

    List<RedirectRuleEntity> findByDynamicQrCodeIdOrderByPriorityDesc( Long dynamicQrCodeId );
}
