package io.zaplink.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.core.common.enums.CampaignStatus;
import io.zaplink.core.common.enums.CampaignType;
import io.zaplink.core.entity.Campaign;

@Repository
public interface QrCampaignRepository
    extends
    JpaRepository<Campaign, Long>
{
    Optional<Campaign> findByCampaignId( String campaignId );

    List<Campaign> findByUserEmail( String userEmail );

    List<Campaign> findByTypeAndUserEmail( CampaignType type, String userEmail );

    @Query("SELECT c FROM Campaign c WHERE c.userEmail = :userEmail AND c.type = :type AND c.status = :status")
    List<Campaign> findByUserEmailAndTypeAndStatus( @Param("userEmail") String userEmail,
                                                    @Param("type") CampaignType type,
                                                    @Param("status") CampaignStatus status );

    @Query("SELECT c FROM Campaign c WHERE c.userEmail = :userEmail AND c.type = :type AND c.status = 'ACTIVE'")
    List<Campaign> findActiveQrCampaignsByUserEmail( @Param("userEmail") String userEmail );

    @Query("SELECT COUNT(c) FROM Campaign c WHERE c.userEmail = :userEmail AND c.type = :type AND c.status = 'ACTIVE'")
    Long countActiveQrCampaignsByUserEmail( @Param("userEmail") String userEmail );

    // Legacy method for backward compatibility
    @Query("SELECT c FROM Campaign c WHERE c.userEmail = :userEmail AND c.type = 'QR_CODE' AND c.status = 'ACTIVE'")
    List<Campaign> findActiveByUserEmail( @Param("userEmail") String userEmail );

    // Legacy method for backward compatibility
    @Query("SELECT COUNT(c) FROM Campaign c WHERE c.userEmail = :userEmail AND c.type = 'QR_CODE' AND c.status = 'ACTIVE'")
    Long countActiveByUserEmail( @Param("userEmail") String userEmail );
}
