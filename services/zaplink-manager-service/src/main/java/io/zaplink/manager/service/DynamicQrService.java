package io.zaplink.manager.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import io.zaplink.core.grpc.CoreServiceProto;
import io.zaplink.manager.common.client.CoreQrGrpcClient;
import io.zaplink.manager.common.client.CoreServiceClient;
import io.zaplink.manager.dto.request.qr.QRConfig;
import io.zaplink.manager.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.manager.dto.response.dynamicqr.QrAnalyticsResponse;
import io.zaplink.manager.repository.QrScanAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for Dynamic QR operations.
 * Uses gRPC to fetch QR data from Core Service.
 * Analytics data is fetched from local repository.
 */
@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrService
{
    private final CoreServiceClient         coreServiceClient;
    private final CoreQrGrpcClient          coreQrGrpcClient;
    private final ObjectMapper              objectMapper;
    private final QrScanAnalyticsRepository qrScanAnalyticsRepository;
    private static final DateTimeFormatter  FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
    public Optional<DynamicQrResponse> getDynamicQr( String qrKey, String userEmail )
    {
        try
        {
            CoreServiceProto.DynamicQrData qrData = coreQrGrpcClient.getDynamicQrByKey( qrKey, userEmail );
            return Optional.of( convertGrpcToResponse( qrData ) );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QR by key via gRPC: {}", qrKey, e );
            return Optional.empty();
        }
    }

    public Page<DynamicQrResponse> getDynamicQrsByUser( String userEmail, Pageable pageable )
    {
        try
        {
            if ( userEmail == null || userEmail.trim().isEmpty() )
            {
                return new PageImpl<>( Collections.emptyList(), pageable, 0 );
            }
            // Call Core Service via gRPC
            CoreServiceProto.GetDynamicQrsResponse grpcResponse = coreQrGrpcClient
                    .getDynamicQrsByUser( userEmail, pageable.getPageNumber(), pageable.getPageSize() );
            List<DynamicQrResponse> responses = grpcResponse.getQrsList().stream().map( this::convertGrpcToResponse )
                    .collect( Collectors.toList() );
            return new PageImpl<>( responses, pageable, grpcResponse.getTotalCount() );
        }
        catch ( Exception e )
        {
            log.error( "Error getting dynamic QRs by user via gRPC: {}", userEmail, e );
            return new PageImpl<>( Collections.emptyList(), pageable, 0 );
        }
    }

    public QrAnalyticsResponse getQrAnalytics( String qrKey,
                                               String userEmail,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate )
    {
        // Get QR data from Core via gRPC
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
        // Get QR data from Core via gRPC
        try
        {
            CoreServiceProto.DynamicQrData qrData = coreQrGrpcClient.getDynamicQrByKey( qrKey, userEmail );
            String redirectUrl = generateRedirectUrl( qrKey );
            // Parse QR config from JSON
            QRConfig qrConfig = objectMapper.readValue( qrData.getQrConfig(), QRConfig.class );
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
            log.error( "Error generating QR image via gRPC", e );
            throw new RuntimeException( "Failed to generate QR image", e );
        }
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "https://zaplink.app/r/" + qrKey;
    }

    private DynamicQrResponse convertGrpcToResponse( CoreServiceProto.DynamicQrData grpcData )
    {
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        LocalDateTime lastScanned = null;
        try
        {
            if ( !grpcData.getCreatedAt().isEmpty() )
                createdAt = LocalDateTime.parse( grpcData.getCreatedAt(), FORMATTER );
            if ( !grpcData.getUpdatedAt().isEmpty() )
                updatedAt = LocalDateTime.parse( grpcData.getUpdatedAt(), FORMATTER );
            if ( !grpcData.getLastScanned().isEmpty() )
                lastScanned = LocalDateTime.parse( grpcData.getLastScanned(), FORMATTER );
        }
        catch ( Exception e )
        {
            log.warn( "Error parsing date from gRPC response", e );
        }
        return new DynamicQrResponse( grpcData.getId(),
                                      grpcData.getQrKey(),
                                      grpcData.getQrName(),
                                      grpcData.getCurrentDestinationUrl(),
                                      grpcData.getQrImageUrl(),
                                      grpcData.getRedirectUrl(),
                                      grpcData.getCampaignId(),
                                      grpcData.getUserEmail(),
                                      grpcData.getIsActive(),
                                      grpcData.getTotalScans(),
                                      createdAt,
                                      updatedAt,
                                      lastScanned );
    }
}
