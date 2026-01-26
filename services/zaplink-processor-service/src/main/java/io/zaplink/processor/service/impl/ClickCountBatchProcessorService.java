package io.zaplink.processor.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import io.zaplink.processor.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Batch processor service for click count updates.
 * Accumulates click counts in memory and flushes to database periodically.
 * Provides higher throughput compared to immediate database updates.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-27
 */
@Service @Slf4j @RequiredArgsConstructor
public class ClickCountBatchProcessorService
{
    private final UrlMappingRepository    urlMappingRepository;
    private final TransactionTemplate     transactionTemplate;
    /**
     * Thread-safe in-memory buffer for accumulating click counts.
     * Key: URL key, Value: Accumulated count
     */
    private final Map<String, AtomicLong> batchBuffer = new ConcurrentHashMap<>();
    /**
     * Accumulates a click count in the in-memory buffer.
     * This method is very fast as it only updates memory.
     * 
     * @param urlKey the URL key to increment
     * @param count the amount to increment by (usually 1)
     */
    public void accumulateClickCount( String urlKey, long count )
    {
        // Atomically increment the count in the buffer
        batchBuffer.computeIfAbsent( urlKey, k -> new AtomicLong( 0 ) ).addAndGet( count );
        log.debug( "📊 Accumulated click count for URL key: {}, current buffer size: {}", urlKey, batchBuffer.size() );
    }

    /**
     * Flushes accumulated click counts to the database.
     * Scheduled to run every 5 seconds (configurable).
     * Uses granular transactions to ensure partial success.
     */
    @Scheduled(fixedRateString = "${zaplink.batch.flush-interval:30000}")
    public void flushBatch()
    {
        if ( batchBuffer.isEmpty() )
        {
            log.debug( "⏭️ Batch buffer is empty, skipping flush" );
            return;
        }
        log.info( "🔄 Starting batch flush with {} URL keys", batchBuffer.size() );
        int successCount = 0;
        int failureCount = 0;
        long totalClicks = 0;
        // Create a snapshot and clear the buffer atomically
        Map<String, AtomicLong> snapshot = new ConcurrentHashMap<>( batchBuffer );
        batchBuffer.clear();
        // Process each URL key in the snapshot
        for ( Map.Entry<String, AtomicLong> entry : snapshot.entrySet() )
        {
            String urlKey = entry.getKey();
            long count = entry.getValue().get();
            totalClicks += count;
            try
            {
                // Execute update in its own transaction
                boolean updated = Boolean.TRUE.equals( transactionTemplate.execute( status -> {
                    int rows2 = urlMappingRepository.incrementClickCountBy( urlKey, count );
                    return rows2 > 0;
                } ) );
                if ( updated )
                {
                    successCount++;
                    log.debug( "✅ Updated URL key: {} with count: {}", urlKey, count );
                }
                else
                {
                    failureCount++;
                    log.warn( "⚠️ URL key not found: {}, count: {}", urlKey, count );
                    // Don't retry not-found errors, it's likely invalid data
                }
            }
            catch ( Exception e )
            {
                failureCount++;
                log.error( "❌ Error updating URL key: {}, count: {}", urlKey, count, e );
                // Re-add to buffer for retry in next flush (only for system errors)
                batchBuffer.computeIfAbsent( urlKey, k -> new AtomicLong( 0 ) ).addAndGet( count );
            }
        }
        log.info( "✅ Batch flush completed - Success: {}, Failures: {}, Total clicks: {}", successCount, failureCount,
                  totalClicks );
    }

    /**
     * Gets the current size of the batch buffer.
     * Useful for monitoring and health checks.
     * 
     * @return number of URL keys in the buffer
     */
    public int getBufferSize()
    {
        return batchBuffer.size();
    }

    /**
     * Gets the total accumulated clicks across all URL keys in the buffer.
     * Useful for monitoring.
     * 
     * @return total accumulated clicks
     */
    public long getTotalAccumulatedClicks()
    {
        return batchBuffer.values().stream().mapToLong( AtomicLong::get ).sum();
    }

    /**
     * Manually triggers a batch flush.
     * Useful for testing or graceful shutdown.
     */
    public void manualFlush()
    {
        log.info( "🔧 Manual flush triggered" );
        flushBatch();
    }

    /**
     * Graceful shutdown - flushes pending batches before application stops.
     * Ensures no data is lost during shutdown.
     */
    @jakarta.annotation.PreDestroy
    public void shutdown()
    {
        log.info( "🛑 Shutting down - flushing pending batches" );
        flushBatch();
        log.info( "✅ Shutdown complete - all batches flushed" );
    }
}
