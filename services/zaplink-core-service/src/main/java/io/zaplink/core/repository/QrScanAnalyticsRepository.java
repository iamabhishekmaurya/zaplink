package io.zaplink.core.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.QrScanAnalyticsEntity;

@Repository
public interface QrScanAnalyticsRepository extends JpaRepository<QrScanAnalyticsEntity, Long> {
    
    List<QrScanAnalyticsEntity> findByQrKey(String qrKey);
    
    @Query("SELECT COUNT(a) FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey")
    Long countByQrKey(@Param("qrKey") String qrKey);
    
    @Query("SELECT COUNT(a) FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey AND a.scannedAt BETWEEN :startDate AND :endDate")
    Long countByQrKeyAndDateRange(@Param("qrKey") String qrKey, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a.country, COUNT(a) FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey GROUP BY a.country")
    List<Object[]> getCountryStats(@Param("qrKey") String qrKey);
    
    @Query("SELECT a.deviceType, COUNT(a) FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey GROUP BY a.deviceType")
    List<Object[]> getDeviceStats(@Param("qrKey") String qrKey);
    
    @Query("SELECT a.browser, COUNT(a) FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey GROUP BY a.browser")
    List<Object[]> getBrowserStats(@Param("qrKey") String qrKey);
    
    @Query("SELECT DATE(a.scannedAt), COUNT(a) FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey AND a.scannedAt >= :startDate GROUP BY DATE(a.scannedAt) ORDER BY DATE(a.scannedAt)")
    List<Object[]> getDailyScanStats(@Param("qrKey") String qrKey, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT a FROM QrScanAnalyticsEntity a WHERE a.qrKey = :qrKey ORDER BY a.scannedAt DESC")
    List<QrScanAnalyticsEntity> findRecentScans(@Param("qrKey") String qrKey);
}
