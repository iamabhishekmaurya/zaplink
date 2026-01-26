package io.zaplink.processor.service.helper;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.processor.entity.UrlAnalyticsEntity;
import io.zaplink.processor.repository.UrlAnalyticsRepository;
import io.zaplink.processor.service.impl.ClickCountBatchProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka listener for consuming click count messages.
 * Uses batch processing for higher throughput.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-26
 */
@Service @Slf4j @RequiredArgsConstructor
public class KafkaListenerHelper
{
    private final ClickCountBatchProcessorService batchProcessorService;
    private final UrlAnalyticsRepository          analyticsRepository;
    /**
     * Listens to URL click events from Kafka.
     * 1. Saves detailed analytics.
     * 2. Accumulates click count for batch update.
     * 
     * @param event the URL click event record
     */
    @KafkaListener(topics = "url-click-events", groupId = "zaplink-clickcount-consumer-group-v3")
    public void handleUrlClickEvent( io.zaplink.processor.dto.event.UrlClickEvent event )
    {
        log.debug( "📨 Received URL click event: {}", event );
        try
        {
            saveUrlAnalytics( event );
            // Accumulate click count
            batchProcessorService.accumulateClickCount( event.urlKey(), 1 );
        }
        catch ( Exception e )
        {
            log.error( "❌ Error processing URL click event for key: {}", event.urlKey(), e );
        }
    }

    /**
     * Listens to QR scan events from Kafka.
     * 1. Saves detailed analytics.
     * 2. Accumulates scan count (as click count) for batch update.
     * 
     * @param event the QR scan event record
     */
    @KafkaListener(topics = "qr-scan-events", groupId = "zaplink-analytics-consumer-group-v3")
    public void handleQrScanEvent( io.zaplink.processor.dto.event.QrScanEvent event )
    {
        log.debug( "📨 Received QR scan event: {}", event );
        try
        {
            saveQrAnalytics( event );
            // Accumulate scan count (treated as click for now)
            batchProcessorService.accumulateClickCount( event.qrKey(), 1 );
        }
        catch ( Exception e )
        {
            log.error( "❌ Error processing QR scan event for key: {}", event.qrKey(), e );
        }
    }

    private void saveUrlAnalytics( io.zaplink.processor.dto.event.UrlClickEvent event )
    {
        UrlAnalyticsEntity entity = new UrlAnalyticsEntity();
        entity.setShortUrlKey( event.urlKey() );
        entity.setIpAddress( event.ipAddress() );
        entity.setUserAgent( event.userAgent() );
        entity.setReferrer( event.referrer() );
        entity.setDeviceType( event.deviceType() );
        entity.setBrowser( event.browser() );
        entity.setTraceId( event.traceId() );
        // Convert Instant to LocalDateTime
        if ( event.timestamp() != null )
        {
            entity.setAccessedAt( java.time.LocalDateTime.ofInstant( event.timestamp(),
                                                                     java.time.ZoneId.of( "UTC" ) ) );
        }
        else
        {
            entity.setAccessedAt( java.time.LocalDateTime.now() );
        }
        entity.setCountry( event.country() );
        entity.setCity( event.city() );
        analyticsRepository.save( entity );
    }

    private void saveQrAnalytics( io.zaplink.processor.dto.event.QrScanEvent event )
    {
        UrlAnalyticsEntity entity = new UrlAnalyticsEntity();
        entity.setShortUrlKey( event.qrKey() ); // Storing QR key in short_url_key column
        entity.setIpAddress( event.ipAddress() );
        entity.setUserAgent( event.userAgent() );
        entity.setReferrer( event.referrer() );
        entity.setDeviceType( event.deviceType() );
        entity.setBrowser( event.browser() );
        entity.setTraceId( event.traceId() );
        if ( event.timestamp() != null )
        {
            entity.setAccessedAt( java.time.LocalDateTime.ofInstant( event.timestamp(),
                                                                     java.time.ZoneId.of( "UTC" ) ) );
        }
        else
        {
            entity.setAccessedAt( java.time.LocalDateTime.now() );
        }
        entity.setCountry( event.country() );
        entity.setCity( event.city() );
        analyticsRepository.save( entity );
    }
}
