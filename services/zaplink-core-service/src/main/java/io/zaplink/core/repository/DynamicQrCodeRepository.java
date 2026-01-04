package io.zaplink.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.DynamicQrCodeEntity;

@Repository
public interface DynamicQrCodeRepository
    extends
    JpaRepository<DynamicQrCodeEntity, Long>
{
    Optional<DynamicQrCodeEntity> findByQrKey( String qrKey );

    List<DynamicQrCodeEntity> findByUserEmail( String userEmail );

    Page<DynamicQrCodeEntity> findByUserEmail( String userEmail, Pageable pageable );

    List<DynamicQrCodeEntity> findByCampaignId( String campaignId );

    @Query("SELECT d FROM DynamicQrCodeEntity d WHERE d.isActive = true AND d.userEmail = :userEmail")
    List<DynamicQrCodeEntity> findActiveByUserEmail( @Param("userEmail") String userEmail );

    @Query("SELECT COUNT(d) FROM DynamicQrCodeEntity d WHERE d.userEmail = :userEmail AND d.isActive = true")
    Long countActiveByUserEmail( @Param("userEmail") String userEmail );

    @Query("SELECT d FROM DynamicQrCodeEntity d WHERE d.lastScanned BETWEEN :startDate AND :endDate")
    List<DynamicQrCodeEntity> findRecentlyScanned( @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate );

    @Query("SELECT d FROM DynamicQrCodeEntity d WHERE d.createdAt BETWEEN :startDate AND :endDate")
    List<DynamicQrCodeEntity> findByCreationDateRange( @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate );
}
