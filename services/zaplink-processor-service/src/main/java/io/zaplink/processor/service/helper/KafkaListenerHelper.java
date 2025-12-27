package io.zaplink.processor.service.helper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.processor.dto.request.ClickCount;
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
}
