package io.zaplink.manager.service.dynamicqr.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.zaplink.manager.dto.request.qr.QRConfig;
import io.zaplink.manager.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.manager.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.manager.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.manager.entity.DynamicQrCodeEntity;
import io.zaplink.manager.repository.DynamicQrCodeRepository;
import io.zaplink.manager.repository.QrScanAnalyticsRepository;
import io.zaplink.manager.client.CoreServiceClient;
import io.zaplink.manager.service.dynamicqr.DynamicQrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrServiceImpl
    implements
    DynamicQrService
{
    private final DynamicQrCodeRepository   dynamicQrCodeRepository;
    private final CoreServiceClient         coreServiceClient;
    private final ObjectMapper              objectMapper;
    private final QrScanAnalyticsRepository qrScanAnalyticsRepository;
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
                return new PageImpl<>( java.util.Collections.emptyList(), pageable, 0 );
            }
            return dynamicQrCodeRepository.findByUserEmail( userEmail, pageable ).map( this::convertToResponse );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QRs by user: {}", userEmail, e );
            return new PageImpl<>( java.util.Collections.emptyList(), pageable, 0 );
        }
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
            qrConfig.setData( redirectUrl );
            return coreServiceClient.generateStyledQr( qrConfig ).getBody();
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

    @Override
    public DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail )
    {
        try
        {
            DynamicQrCodeEntity entity = new DynamicQrCodeEntity();
            entity.setQrKey( generateUniqueKey() );
            entity.setQrName( request.getQrName() );
            entity.setCurrentDestinationUrl( request.getDestinationUrl() );
            entity.setUserEmail( userEmail );
            entity.setCampaignId( request.getCampaignId() );
            entity.setIsActive( true );
            entity.setCreatedAt( LocalDateTime.now() );
            entity.setUpdatedAt( LocalDateTime.now() );
            entity.setTotalScans( 0L );
            // Advanced features
            entity.setPassword( request.getPassword() );
            entity.setScanLimit( request.getScanLimit() );
            entity.setExpirationDate( request.getExpirationDate() );
            if ( request.getAllowedDomains() != null )
            {
                entity.setAllowedDomains( objectMapper.writeValueAsString( request.getAllowedDomains() ) );
            }
            // Save Config
            if ( request.getQrConfig() != null )
            {
                entity.setQrConfig( objectMapper.writeValueAsString( request.getQrConfig() ) );
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
        DynamicQrResponse response = new DynamicQrResponse();
        response.setId( entity.getId() );
        response.setQrKey( entity.getQrKey() );
        response.setQrName( entity.getQrName() );
        response.setCurrentDestinationUrl( entity.getCurrentDestinationUrl() );
        response.setQrImageUrl( "/api/rd/dyqr/" + entity.getQrKey() + "/image" );
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
