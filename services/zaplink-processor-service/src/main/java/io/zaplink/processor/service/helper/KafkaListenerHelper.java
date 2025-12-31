package io.zaplink.processor.service.helper;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.processor.dto.request.AnalyticsEvent;
import io.zaplink.processor.dto.request.ClickCount;
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
    private final ClickCountBatchProcessorService           batchProcessorService;
    private final UrlAnalyticsRepository                    analyticsRepository;
    private final io.zaplink.processor.service.GeoIpService geoIpService;
    /**
     * Listens to click count messages from Kafka and accumulates them in memory.
     * Actual database updates happen periodically via scheduled batch flush.
     * 
     * @param clickCount the click count data from Kafka
     */
    @KafkaListener(topics = "url-clickcount-topic", groupId = "zaplink-clickcount-consumer-group")
    public void handleClickCount( ClickCount clickCount )
    {
        log.debug( "📨 Received click count message: {}", clickCount );
        batchProcessorService.accumulateClickCount( clickCount );
    }

    @KafkaListener(topics = "url-analytics-topic", groupId = "zaplink-analytics-consumer-group")
    public void handleAnalyticsEvent( AnalyticsEvent event )
    {
        log.debug( "📨 Received analytics event: {}", event );
        // 1. Save detailed analytics
        UrlAnalyticsEntity entity = new UrlAnalyticsEntity();
        entity.setShortUrlKey( event.getUrlKey() );
        entity.setIpAddress( event.getIpAddress() );
        entity.setUserAgent( event.getUserAgent() );
        entity.setReferrer( event.getReferrer() );
        // Extract country/city from IP
        Map<String, String> location = geoIpService.resolveLocation( event.getIpAddress() );
        if ( location != null )
        {
            entity.setCountry( location.getOrDefault( "country", "Unknown" ) );
            entity.setCity( location.getOrDefault( "city", "Unknown" ) );
        }
        entity.setAccessedAt( event.getTimestamp() );
        try
        {
            analyticsRepository.save( entity );
        }
        catch ( Exception e )
        {
            log.error( "Error saving analytics", e );
        }
        // 2. Update click count (legacy support / dashboard)
        ClickCount clickCount = new ClickCount();
        clickCount.setUrlKey( event.getUrlKey() );
        clickCount.setCount( 1 );
        batchProcessorService.accumulateClickCount( clickCount );
    }
}
