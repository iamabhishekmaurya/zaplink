package io.zaplink.manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.manager.common.client.CoreServiceClient;
import io.zaplink.manager.dto.request.qr.QRConfig;
import io.zaplink.manager.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.manager.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.manager.entity.DynamicQrCodeEntity;
import io.zaplink.manager.repository.DynamicQrCodeRepository;
import io.zaplink.manager.repository.QrScanAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for Dynamic QR operations.
 * Uses direct database access for QR data.
 * Analytics data is fetched from local repository.
 */
@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrService
{
    private final CoreServiceClient         coreServiceClient;
    private final DynamicQrCodeRepository   dynamicQrCodeRepository;
    private final ObjectMapper              objectMapper;
    private final QrScanAnalyticsRepository qrScanAnalyticsRepository;
    public Optional<DynamicQrResponse> getDynamicQr( String qrKey, String userEmail )
    {
        try
        {
            Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
            if ( entityOpt.isEmpty() )
            {
                return Optional.empty();
            }
            DynamicQrCodeEntity entity = entityOpt.get();
            // Verify ownership if userEmail provided
            if ( userEmail != null && !userEmail.isEmpty() && !userEmail.equals( entity.getUserEmail() ) )
            {
                log.warn( "Access denied to QR: {} for user: {}", qrKey, userEmail );
                return Optional.empty();
            }
            return Optional.of( convertEntityToResponse( entity ) );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QR by key: {}", qrKey, e );
            return Optional.empty();
        }
    }

    public Page<DynamicQrResponse> getDynamicQrsByUser( String userEmail, Pageable pageable )
    {
        try
        {
            if ( userEmail == null || userEmail.trim().isEmpty() )
            {
                return Page.empty( pageable );
            }
            Page<DynamicQrCodeEntity> entityPage = dynamicQrCodeRepository.findByUserEmail( userEmail, pageable );
            List<DynamicQrResponse> responses = entityPage.getContent().stream().map( this::convertEntityToResponse )
                    .collect( Collectors.toList() );
            return new PageImpl<>( responses, pageable, entityPage.getTotalElements() );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QRs by user: {}", userEmail, e );
            return Page.empty( pageable );
        }
    }

    public QrAnalyticsResponse getQrAnalytics( String qrKey,
                                               String userEmail,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate )
    {
        // Get QR data from DB
        Optional<DynamicQrResponse> qrOpt = getDynamicQr( qrKey, userEmail );
        if ( qrOpt.isEmpty() )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrResponse qrData = qrOpt.get();
        // Date Ranges
        if ( startDate == null )
            startDate = LocalDateTime.now().minusDays( 30 );
        if ( endDate == null )
            endDate = LocalDateTime.now();
        // Calculate Scans Today, Week, Month
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusWeeks( 1 );
        LocalDateTime monthStart = now.minusMonths( 1 );
        // Map Country Stats
        List<Object[]> countryData = qrScanAnalyticsRepository.getCountryStats( qrKey );
        List<QrAnalyticsResponse.CountryStats> countryStats = countryData.stream()
                .map( obj -> new QrAnalyticsResponse.CountryStats( (String) obj[0],
                                                                   (Long) obj[1],
                                                                   qrData.totalScans() > 0 ? (double) (Long) obj[1]
                                                                           / qrData.totalScans() * 100 : 0 ) )
                .collect( Collectors.toList() );
        // Map Device Stats
        List<Object[]> deviceData = qrScanAnalyticsRepository.getDeviceStats( qrKey );
        List<QrAnalyticsResponse.DeviceStats> deviceStats = deviceData.stream()
                .map( obj -> new QrAnalyticsResponse.DeviceStats( (String) obj[0],
                                                                  (Long) obj[1],
                                                                  qrData.totalScans() > 0 ? (double) (Long) obj[1]
                                                                          / qrData.totalScans() * 100 : 0 ) )
                .collect( Collectors.toList() );
        // Map Browser Stats
        List<Object[]> browserData = qrScanAnalyticsRepository.getBrowserStats( qrKey );
        List<QrAnalyticsResponse.BrowserStats> browserStats = browserData.stream()
                .map( obj -> new QrAnalyticsResponse.BrowserStats( (String) obj[0],
                                                                   (Long) obj[1],
                                                                   qrData.totalScans() > 0 ? (double) (Long) obj[1]
                                                                           / qrData.totalScans() * 100 : 0 ) )
                .collect( Collectors.toList() );
        // Map Daily Stats
        List<Object[]> dailyData = qrScanAnalyticsRepository.getDailyScanStats( qrKey, startDate );
        List<QrAnalyticsResponse.DailyStats> dailyStats = dailyData.stream()
                .map( obj -> new QrAnalyticsResponse.DailyStats( obj[0].toString(), (Long) obj[1] ) )
                .collect( Collectors.toList() );
        return new QrAnalyticsResponse( qrData.qrKey(),
                                        qrData.qrName(),
                                        qrData.totalScans(),
                                        qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, todayStart, now ),
                                        qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, weekStart, now ),
                                        qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, monthStart, now ),
                                        qrData.lastScanned(),
                                        countryStats,
                                        deviceStats,
                                        browserStats,
                                        dailyStats );
    }

    public byte[] generateQrImage( String qrKey, String userEmail )
    {
        // Get QR data from DB
        try
        {
            DynamicQrCodeEntity entity = dynamicQrCodeRepository.findByQrKey( qrKey )
                    .orElseThrow( () -> new RuntimeException( "Dynamic QR not found: " + qrKey ) );
            // Verify ownership
            if ( userEmail != null && !userEmail.isEmpty() && !userEmail.equals( entity.getUserEmail() ) )
            {
                throw new RuntimeException( "Access denied to QR: " + qrKey );
            }
            String redirectUrl = generateRedirectUrl( qrKey );
            // Parse QR config from JSON
            QRConfig qrConfig = objectMapper.readValue( entity.getQrConfig(), QRConfig.class );
            // Create new QRConfig with updated data since records are immutable
            QRConfig updatedQrConfig = new QRConfig( redirectUrl,
                                                     qrConfig.size(),
                                                     qrConfig.margin(),
                                                     qrConfig.errorCorrectionLevel(),
                                                     qrConfig.transparentBackground(),
                                                     qrConfig.backgroundColor(),
                                                     qrConfig.body(),
                                                     qrConfig.eye(),
                                                     qrConfig.logo() );
            return coreServiceClient.generateStyledQr( updatedQrConfig ).getBody();
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error parsing QR config from JSON", e );
            throw new RuntimeException( "Failed to parse QR configuration", e );
        }
        catch ( Exception e )
        {
            log.error( "Error generating QR image", e );
            throw new RuntimeException( "Failed to generate QR image", e );
        }
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "http://localhost:8090/s/" + qrKey;
    }

    private DynamicQrResponse convertEntityToResponse( DynamicQrCodeEntity entity )
    {
        log.info( "Converting entity to response: {}", entity.getQrKey() );
        QRConfig qrConfig = null;
        try
        {
            qrConfig = objectMapper.readValue( entity.getQrConfig(), QRConfig.class );
        }
        catch ( JsonMappingException e )
        {
            log.error( "Error parsing QR config from JSON", e );
            throw new RuntimeException( "Failed to parse QR configuration", e );
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error parsing QR config from JSON", e );
            throw new RuntimeException( "Failed to parse QR configuration", e );
        }
        return new DynamicQrResponse( entity.getId(),
                                      entity.getQrKey(),
                                      entity.getQrName(),
                                      entity.getCurrentDestinationUrl(),
                                      qrConfig,
                                      generateRedirectUrl( entity.getQrKey() ),
                                      entity.getCampaignId(),
                                      entity.getUserEmail(),
                                      entity.getIsActive(),
                                      entity.getTotalScans(),
                                      entity.getCreatedAt(),
                                      entity.getUpdatedAt(),
                                      entity.getLastScanned() );
    }
}
