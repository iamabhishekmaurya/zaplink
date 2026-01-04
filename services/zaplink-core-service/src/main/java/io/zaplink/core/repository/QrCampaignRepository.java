package io.zaplink.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.QrCampaignEntity;

@Repository
public interface QrCampaignRepository extends JpaRepository<QrCampaignEntity, Long> {
    
    Optional<QrCampaignEntity> findByCampaignId(String campaignId);
    
    List<QrCampaignEntity> findByUserEmail(String userEmail);
    
    @Query("SELECT c FROM QrCampaignEntity c WHERE c.userEmail = :userEmail AND c.status = 'ACTIVE'")
    List<QrCampaignEntity> findActiveByUserEmail(@Param("userEmail") String userEmail);
    
    @Query("SELECT COUNT(c) FROM QrCampaignEntity c WHERE c.userEmail = :userEmail AND c.status = 'ACTIVE'")
    Long countActiveByUserEmail(@Param("userEmail") String userEmail);
}
