package io.zaplink.manager.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.manager.common.client.CoreServiceClient;
import io.zaplink.manager.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.manager.dto.request.qr.QRConfig;
import io.zaplink.manager.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.manager.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.manager.entity.DynamicQrCodeEntity;
import io.zaplink.manager.repository.DynamicQrCodeRepository;
import io.zaplink.manager.repository.QrScanAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrService
{
    private final DynamicQrCodeRepository   dynamicQrCodeRepository;
    private final CoreServiceClient         coreServiceClient;
    private final ObjectMapper              objectMapper;
    private final QrScanAnalyticsRepository qrScanAnalyticsRepository;
    public Optional<DynamicQrResponse> getDynamicQr( String qrKey, String userEmail )
    {
        return dynamicQrCodeRepository.findByQrKey( qrKey ).filter( qr -> qr.getUserEmail().equals( userEmail ) )
                .map( this::convertToResponse );
    }

    public Page<DynamicQrResponse> getDynamicQrsByUser( String userEmail, Pageable pageable )
    {
        try
        {
            if ( userEmail == null || userEmail.trim().isEmpty() )
            {
                return new PageImpl<>( Collections.emptyList(), pageable, 0 );
            }
            return dynamicQrCodeRepository.findByUserEmail( userEmail, pageable ).map( this::convertToResponse );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QRs by user: {}", userEmail, e );
            return new PageImpl<>( Collections.emptyList(), pageable, 0 );
        }
    }

    public QrAnalyticsResponse getQrAnalytics( String qrKey,
                                               String userEmail,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity qrEntity = entityOpt.get();
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
        // Aggregate Stats from Repository
        // Map Country Stats
        List<Object[]> countryData = qrScanAnalyticsRepository.getCountryStats( qrKey );
        List<QrAnalyticsResponse.CountryStats> countryStats = countryData.stream()
                .map( obj -> new QrAnalyticsResponse.CountryStats( (String) obj[0],
                                                                   (Long) obj[1],
                                                                   qrEntity.getTotalScans() > 0 ? (double) (Long) obj[1]
                                                                           / qrEntity.getTotalScans() * 100 : 0 ) )
                .collect( Collectors.toList() );
        // Map Device Stats
        List<Object[]> deviceData = qrScanAnalyticsRepository.getDeviceStats( qrKey );
        List<QrAnalyticsResponse.DeviceStats> deviceStats = deviceData.stream()
                .map( obj -> new QrAnalyticsResponse.DeviceStats( (String) obj[0],
                                                                  (Long) obj[1],
                                                                  qrEntity.getTotalScans() > 0 ? (double) (Long) obj[1]
                                                                          / qrEntity.getTotalScans() * 100 : 0 ) )
                .collect( Collectors.toList() );
        // Map Browser Stats
        List<Object[]> browserData = qrScanAnalyticsRepository.getBrowserStats( qrKey );
        List<QrAnalyticsResponse.BrowserStats> browserStats = browserData.stream()
                .map( obj -> new QrAnalyticsResponse.BrowserStats( (String) obj[0],
                                                                   (Long) obj[1],
                                                                   qrEntity.getTotalScans() > 0 ? (double) (Long) obj[1]
                                                                           / qrEntity.getTotalScans() * 100 : 0 ) )
                .collect( Collectors.toList() );
        // Map Daily Stats
        List<Object[]> dailyData = qrScanAnalyticsRepository.getDailyScanStats( qrKey, startDate );
        List<QrAnalyticsResponse.DailyStats> dailyStats = dailyData.stream()
                .map( obj -> new QrAnalyticsResponse.DailyStats( obj[0].toString(), (Long) obj[1] ) )
                .collect( Collectors.toList() );
        return new QrAnalyticsResponse( qrEntity.getQrKey(),
                                        qrEntity.getQrName(),
                                        qrEntity.getTotalScans(),
                                        qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, todayStart, now ),
                                        qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, weekStart, now ),
                                        qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, monthStart, now ),
                                        qrEntity.getLastScanned(),
                                        countryStats,
                                        deviceStats,
                                        browserStats,
                                        dailyStats );
    }

    public List<DynamicQrResponse> getQrCodesByCampaign( String campaignId, String userEmail )
    {
        return dynamicQrCodeRepository.findByCampaignId( campaignId ).stream()
                .filter( qr -> qr.getUserEmail().equals( userEmail ) ).map( this::convertToResponse )
                .collect( Collectors.toList() );
    }

    public byte[] generateQrImage( String qrKey, String userEmail )
    {
        // For generating image, we don't necessarily enforce user ownership strictly if public
        // But for editing/managing, we do.
        // If we want public access (like embedding in emails), we might relax userEmail check or make it optional
        // Based on original code, it seems the image endpoint was authenticated.
        // However, usually QR images are public.
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() )
        {
            throw new IllegalArgumentException( "Dynamic QR not found" );
        }
        try
        {
            DynamicQrCodeEntity entity = entityOpt.get();
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
    }

    public long countAllQrCodes()
    {
        return dynamicQrCodeRepository.count();
    }

    public DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail )
    {
        try
        {
            DynamicQrCodeEntity entity = new DynamicQrCodeEntity();
            entity.setQrKey( generateUniqueKey() );
            entity.setQrName( request.qrName() );
            entity.setCurrentDestinationUrl( request.destinationUrl() );
            entity.setUserEmail( userEmail );
            entity.setCampaignId( request.campaignId() );
            entity.setIsActive( true );
            entity.setCreatedAt( LocalDateTime.now() );
            entity.setUpdatedAt( LocalDateTime.now() );
            entity.setTotalScans( 0L );
            // Advanced features
            entity.setPassword( request.password() );
            entity.setScanLimit( request.scanLimit() );
            entity.setExpirationDate( request.expirationDate() );
            if ( request.allowedDomains() != null )
            {
                entity.setAllowedDomains( objectMapper.writeValueAsString( request.allowedDomains() ) );
            }
            // Save Config
            if ( request.qrConfig() != null )
            {
                entity.setQrConfig( objectMapper.writeValueAsString( request.qrConfig() ) );
            }
            DynamicQrCodeEntity saved = dynamicQrCodeRepository.save( entity );
            return convertToResponse( saved );
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error processing JSON for create dynamic QR", e );
            throw new RuntimeException( "Failed to create dynamic QR", e );
        }
    }

    private String generateUniqueKey()
    {
        return java.util.UUID.randomUUID().toString().substring( 0, 8 );
    }

    private DynamicQrResponse convertToResponse( DynamicQrCodeEntity entity )
    {
        return new DynamicQrResponse( entity.getId(),
                                      entity.getQrKey(),
                                      entity.getQrName(),
                                      entity.getCurrentDestinationUrl(),
                                      "/api/rd/dyqr/" + entity.getQrKey() + "/image",
                                      generateRedirectUrl( entity.getQrKey() ),
                                      entity.getCampaignId(),
                                      entity.getUserEmail(),
                                      entity.getIsActive(),
                                      entity.getTotalScans(),
                                      entity.getCreatedAt(),
                                      entity.getUpdatedAt(),
                                      entity.getLastScanned() );
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "https://zaplink.app/r/" + qrKey;
    }
}
