package io.zaplink.auth.common.trace;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Comprehensive test suite for TraceIdConverter.
 * Tests trace ID conversion for logging purposes.
 */
@DisplayName("TraceIdConverter Tests")
class TraceIdConverterTest {

    private TraceIdConverter traceIdConverter;
    
    @Mock
    private ILoggingEvent loggingEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traceIdConverter = new TraceIdConverter();
        TraceContext.clearTraceId();
    }

    @AfterEach
    void tearDown() {
        TraceContext.clearTraceId();
    }

    @Test @DisplayName("Convert should return trace ID when set")
    void convert_ShouldReturnTraceIdWhenSet() {
        // Given
        String expectedTraceId = "test-trace-id-123";
        TraceContext.setTraceId(expectedTraceId);
        
        // When
        String result = traceIdConverter.convert(loggingEvent);
        
        // Then
        assertEquals(expectedTraceId, result);
    }

    @Test @DisplayName("Convert should return N/A when trace ID not set")
    void convert_ShouldReturnNAWhenTraceIdNotSet() {
        // Given - no trace ID set
        
        // When
        String result = traceIdConverter.convert(loggingEvent);
        
        // Then
        assertEquals("N/A", result);
    }

    @Test @DisplayName("Convert should return N/A after trace ID cleared")
    void convert_ShouldReturnNAAfterTraceIdCleared() {
        // Given
        TraceContext.setTraceId("test-trace-id");
        assertEquals("test-trace-id", traceIdConverter.convert(loggingEvent));
        
        // When
        TraceContext.clearTraceId();
        String result = traceIdConverter.convert(loggingEvent);
        
        // Then
        assertEquals("N/A", result);
    }

    @Test @DisplayName("Convert should handle empty trace ID")
    void convert_ShouldHandleEmptyTraceId() {
        // Given
        TraceContext.setTraceId("");
        
        // When
        String result = traceIdConverter.convert(loggingEvent);
        
        // Then
        assertEquals("", result);
    }

    @Test @DisplayName("Convert should work with multiple calls")
    void convert_ShouldWorkWithMultipleCalls() {
        // Given
        String traceId = "multi-call-trace-id";
        TraceContext.setTraceId(traceId);
        
        // When
        String result1 = traceIdConverter.convert(loggingEvent);
        String result2 = traceIdConverter.convert(loggingEvent);
        String result3 = traceIdConverter.convert(loggingEvent);
        
        // Then
        assertEquals(traceId, result1);
        assertEquals(traceId, result2);
        assertEquals(traceId, result3);
    }

    @Test @DisplayName("Convert should work with null logging event")
    void convert_ShouldWorkWithNullLoggingEvent() {
        // Given
        TraceContext.setTraceId("null-event-trace-id");
        
        // When
        String result = traceIdConverter.convert(null);
        
        // Then
        assertEquals("null-event-trace-id", result);
    }

    @Test @DisplayName("Convert should be thread-safe")
    void convert_ShouldBeThreadSafe() throws InterruptedException {
        // Given
        String mainThreadTraceId = "main-thread-trace";
        TraceContext.setTraceId(mainThreadTraceId);
        
        // When - test in separate thread
        String[] threadResult = new String[1];
        Thread testThread = new Thread(() -> {
            TraceContext.setTraceId("thread-trace-id");
            threadResult[0] = traceIdConverter.convert(loggingEvent);
            TraceContext.clearTraceId();
        });
        
        testThread.start();
        testThread.join();
        
        // Then
        assertEquals("thread-trace-id", threadResult[0]);
        assertEquals(mainThreadTraceId, traceIdConverter.convert(loggingEvent));
    }
}
