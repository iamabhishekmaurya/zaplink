package io.zaplink.redirect.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.redirect.common.constants.KafkaTopics;
import io.zaplink.redirect.dto.event.QrScanEvent;
import io.zaplink.redirect.dto.event.UrlClickEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka event publisher for analytics events.
 * Publishes click and scan events asynchronously for processing.
 */
@Service @Slf4j @RequiredArgsConstructor
public class KafkaEventPublisher
{
    private final KafkaTemplate<String, Object> kafkaTemplate;
    /**
     * Publish URL click event to Kafka.
     * Fire-and-forget pattern for non-blocking redirect.
     *
     * @param event the URL click event
     */
    public void publishUrlClickEvent( UrlClickEvent event )
    {
        kafkaTemplate.send( KafkaTopics.URL_CLICK_EVENTS, event.urlKey(), event ).whenComplete( ( result, ex ) -> {
            if ( ex != null )
            {
                log.error( "❌ Failed to publish URL click event for key: {}", event.urlKey(), ex );
            }
            else
            {
                log.debug( "✅ URL click event published for key: {}", event.urlKey() );
            }
        } );
    }

    /**
     * Publish QR scan event to Kafka.
     * Fire-and-forget pattern for non-blocking redirect.
     *
     * @param event the QR scan event
     */
    public void publishQrScanEvent( QrScanEvent event )
    {
        kafkaTemplate.send( KafkaTopics.QR_SCAN_EVENTS, event.qrKey(), event ).whenComplete( ( result, ex ) -> {
            if ( ex != null )
            {
                log.error( "❌ Failed to publish QR scan event for key: {}", event.qrKey(), ex );
            }
            else
            {
                log.debug( "✅ QR scan event published for key: {}", event.qrKey() );
            }
        } );
    }
}
