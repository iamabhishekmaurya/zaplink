package io.zaplink.core.service.dynamicqr.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.core.entity.DynamicQrCodeEntity;
import io.zaplink.core.repository.DynamicQrCodeRepository;
import io.zaplink.core.repository.QrScanAnalyticsRepository;
import io.zaplink.core.service.dynamicqr.DynamicQrService;
import io.zaplink.core.service.qr.QRService;
import io.zaplink.core.utility.SnowflakeShortUrlKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrServiceImpl
    implements
    DynamicQrService
{
    private final DynamicQrCodeRepository   dynamicQrCodeRepository;
    private final QRService                 qrService;
    private final ObjectMapper              objectMapper;
    private final QrScanAnalyticsRepository qrScanAnalyticsRepository;
    @Override @Transactional
    public DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail )
    {
        try
        {
            String qrKey = SnowflakeShortUrlKeyUtil.generateShortKey();
            // Convert QR config to JSON string
            String qrConfigJson = objectMapper.writeValueAsString( request.getQrConfig() );
            DynamicQrCodeEntity entity = DynamicQrCodeEntity.builder().qrKey( qrKey ).qrName( request.getQrName() )
                    .currentDestinationUrl( request.getDestinationUrl() ).qrConfig( qrConfigJson )
                    .userEmail( userEmail ).campaignId( request.getCampaignId() ).isActive( true )
                    .createdAt( LocalDateTime.now() ).updatedAt( LocalDateTime.now() ).totalScans( 0L ).build();
            entity = dynamicQrCodeRepository.save( entity );
            log.info( "Created dynamic QR with key: {} for user: {}", qrKey, userEmail );
            return convertToResponse( entity );
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error converting QR config to JSON", e );
            throw new RuntimeException( "Failed to process QR configuration", e );
        }
    }

    @Override
    public Optional<DynamicQrResponse> getDynamicQr( String qrKey, String userEmail )
    {
        return dynamicQrCodeRepository.findByQrKey( qrKey ).filter( qr -> qr.getUserEmail().equals( userEmail ) )
                .map( this::convertToResponse );
    }

    @Override
    public Page<DynamicQrResponse> getDynamicQrsByUser( String userEmail, Pageable pageable )
    {
        try
        {
            if ( userEmail == null || userEmail.trim().isEmpty() )
            {
                // Return empty page if no user email provided
                return new PageImpl<>( java.util.Collections.emptyList(), pageable, 0 );
            }
            return dynamicQrCodeRepository.findByUserEmail( userEmail, pageable ).map( this::convertToResponse );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QRs by user: {}", userEmail, e );
            // Return empty page on error
            return new PageImpl<>( java.util.Collections.emptyList(), pageable, 0 );
        }
    }

    @Override @Transactional
    public DynamicQrResponse updateDestination( String qrKey, UpdateDestinationRequest request, String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        entity.setCurrentDestinationUrl( request.getDestinationUrl() );
        entity.setUpdatedAt( LocalDateTime.now() );
        entity = dynamicQrCodeRepository.save( entity );
        log.info( "Updated destination for QR key: {} by user: {}", qrKey, userEmail );
        return convertToResponse( entity );
    }

    @Transactional
    public DynamicQrResponse updateQrConfig( String qrKey, String userEmail, String qrConfig )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        entity.setQrConfig( qrConfig );
        entity.setUpdatedAt( LocalDateTime.now() );
        entity = dynamicQrCodeRepository.save( entity );
        log.info( "Updated QR config for QR key: {} by user: {}", qrKey, userEmail );
        return convertToResponse( entity );
    }

    @Override @Transactional
    public void toggleQrStatus( String qrKey, String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        entity.setIsActive( !entity.getIsActive() );
        entity.setUpdatedAt( LocalDateTime.now() );
        dynamicQrCodeRepository.save( entity );
        log.info( "Toggled status for QR key: {} to {} by user: {}", qrKey, entity.getIsActive(), userEmail );
    }

    @Override @Transactional
    public void deleteDynamicQr( String qrKey, String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        dynamicQrCodeRepository.delete( entityOpt.get() );
        log.info( "Deleted dynamic QR with key: {} by user: {}", qrKey, userEmail );
    }

    @Override
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
        QrAnalyticsResponse response = new QrAnalyticsResponse();
        // Basic Info
        response.setQrKey( qrEntity.getQrKey() );
        response.setQrName( qrEntity.getQrName() );
        response.setTotalScans( qrEntity.getTotalScans() );
        response.setLastScanned( qrEntity.getLastScanned() );
        // Date Ranges
        if ( startDate == null )
            startDate = LocalDateTime.now().minusDays( 30 );
        if ( endDate == null )
            endDate = LocalDateTime.now();
        // Calculate Scans Today, Week, Month
        // Note: These should ideally be optimized queries, but for now we can do separate counts or approximations if needed.
        // Or we can rely on the repository's count methods.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusWeeks( 1 );
        LocalDateTime monthStart = now.minusMonths( 1 );
        response.setScansToday( qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, todayStart, now ) );
        response.setScansThisWeek( qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, weekStart, now ) );
        response.setScansThisMonth( qrScanAnalyticsRepository.countByQrKeyAndDateRange( qrKey, monthStart, now ) );
        // Aggregate Stats from Repository
        // Map Country Stats
        List<Object[]> countryData = qrScanAnalyticsRepository.getCountryStats( qrKey );
        List<QrAnalyticsResponse.CountryStats> countryStats = countryData.stream().map( obj -> {
            QrAnalyticsResponse.CountryStats stats = new QrAnalyticsResponse.CountryStats();
            stats.setCountry( (String) obj[0] );
            stats.setCount( (Long) obj[1] );
            stats.setPercentage( qrEntity.getTotalScans() > 0 ? (double) stats.getCount() / qrEntity.getTotalScans()
                    * 100 : 0 );
            return stats;
        } ).collect( Collectors.toList() );
        response.setCountryStats( countryStats );
        // Map Device Stats
        List<Object[]> deviceData = qrScanAnalyticsRepository.getDeviceStats( qrKey );
        List<QrAnalyticsResponse.DeviceStats> deviceStats = deviceData.stream().map( obj -> {
            QrAnalyticsResponse.DeviceStats stats = new QrAnalyticsResponse.DeviceStats();
            stats.setDeviceType( (String) obj[0] );
            stats.setCount( (Long) obj[1] );
            stats.setPercentage( qrEntity.getTotalScans() > 0 ? (double) stats.getCount() / qrEntity.getTotalScans()
                    * 100 : 0 );
            return stats;
        } ).collect( Collectors.toList() );
        response.setDeviceStats( deviceStats );
        // Map Browser Stats
        List<Object[]> browserData = qrScanAnalyticsRepository.getBrowserStats( qrKey );
        List<QrAnalyticsResponse.BrowserStats> browserStats = browserData.stream().map( obj -> {
            QrAnalyticsResponse.BrowserStats stats = new QrAnalyticsResponse.BrowserStats();
            stats.setBrowser( (String) obj[0] );
            stats.setCount( (Long) obj[1] );
            stats.setPercentage( qrEntity.getTotalScans() > 0 ? (double) stats.getCount() / qrEntity.getTotalScans()
                    * 100 : 0 );
            return stats;
        } ).collect( Collectors.toList() );
        response.setBrowserStats( browserStats );
        // Map Daily Stats
        List<Object[]> dailyData = qrScanAnalyticsRepository.getDailyScanStats( qrKey, startDate );
        List<QrAnalyticsResponse.DailyStats> dailyStats = dailyData.stream().map( obj -> {
            QrAnalyticsResponse.DailyStats stats = new QrAnalyticsResponse.DailyStats();
            stats.setDate( obj[0].toString() );
            stats.setScans( (Long) obj[1] );
            return stats;
        } ).collect( Collectors.toList() );
        response.setDailyStats( dailyStats );
        return response;
    }

    @Override
    public List<DynamicQrResponse> getQrCodesByCampaign( String campaignId, String userEmail )
    {
        return dynamicQrCodeRepository.findByCampaignId( campaignId ).stream()
                .filter( qr -> qr.getUserEmail().equals( userEmail ) ).map( this::convertToResponse )
                .collect( Collectors.toList() );
    }

    @Override
    public byte[] generateQrImage( String qrKey, String userEmail )
    {
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
            qrConfig.setData( redirectUrl );
            return qrService.generateStyledQrCode( qrConfig );
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error parsing QR config from JSON", e );
            throw new RuntimeException( "Failed to parse QR configuration", e );
        }
    }

    @Override
    public long countAllQrCodes()
    {
        return dynamicQrCodeRepository.count();
    }

    private DynamicQrResponse convertToResponse( DynamicQrCodeEntity entity )
    {
        DynamicQrResponse response = new DynamicQrResponse();
        response.setId( entity.getId() );
        response.setQrKey( entity.getQrKey() );
        response.setQrName( entity.getQrName() );
        response.setCurrentDestinationUrl( entity.getCurrentDestinationUrl() );
        response.setQrImageUrl( "/api/v1/dynamic-qr/" + entity.getQrKey() + "/image" );
        response.setRedirectUrl( generateRedirectUrl( entity.getQrKey() ) );
        response.setCampaignId( entity.getCampaignId() );
        response.setUserEmail( entity.getUserEmail() );
        response.setIsActive( entity.getIsActive() );
        response.setTotalScans( entity.getTotalScans() );
        response.setCreatedAt( entity.getCreatedAt() );
        response.setUpdatedAt( entity.getUpdatedAt() );
        response.setLastScanned( entity.getLastScanned() );
        return response;
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "https://zaplink.app/r/" + qrKey;
    }
}
