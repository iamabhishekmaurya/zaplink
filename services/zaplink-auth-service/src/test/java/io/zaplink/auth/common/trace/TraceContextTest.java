package io.zaplink.auth.common.trace;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Comprehensive test suite for TraceContext.
 * Tests thread-local trace ID management functionality.
 */
@DisplayName("TraceContext Tests")
class TraceContextTest {

    @BeforeEach
    void setUp() {
        TraceContext.clearTraceId();
    }

    @AfterEach
    void tearDown() {
        TraceContext.clearTraceId();
    }

    @Test @DisplayName("Set and get trace ID should work correctly")
    void setAndGetTraceId_ShouldWorkCorrectly() {
        // Given
        String traceId = "test-trace-id-123";
        
        // When
        TraceContext.setTraceId(traceId);
        String retrievedTraceId = TraceContext.getTraceId();
        
        // Then
        assertEquals(traceId, retrievedTraceId);
    }

    @Test @DisplayName("Get trace ID when not set should return null")
    void getTraceId_WhenNotSet_ShouldReturnNull() {
        // When
        String traceId = TraceContext.getTraceId();
        
        // Then
        assertNull(traceId);
    }

    @Test @DisplayName("Get or generate trace ID should generate when not set")
    void getOrGenerateTraceId_WhenNotSet_ShouldGenerateNewId() {
        // When
        String traceId = TraceContext.getOrGenerateTraceId();
        
        // Then
        assertNotNull(traceId);
        assertFalse(traceId.isEmpty());
        assertEquals(32, traceId.length()); // UUID without dashes
    }

    @Test @DisplayName("Get or generate trace ID should return existing when set")
    void getOrGenerateTraceId_WhenSet_ShouldReturnExistingId() {
        // Given
        String existingTraceId = "existing-trace-id";
        TraceContext.setTraceId(existingTraceId);
        
        // When
        String traceId = TraceContext.getOrGenerateTraceId();
        
        // Then
        assertEquals(existingTraceId, traceId);
    }

    @Test @DisplayName("Clear trace ID should remove thread-local value")
    void clearTraceId_ShouldRemoveThreadLocalValue() {
        // Given
        TraceContext.setTraceId("test-trace-id");
        assertNotNull(TraceContext.getTraceId());
        
        // When
        TraceContext.clearTraceId();
        
        // Then
        assertNull(TraceContext.getTraceId());
    }

    @Test @DisplayName("Clear trace ID when not set should not throw exception")
    void clearTraceId_WhenNotSet_ShouldNotThrowException() {
        // Given - no trace ID set
        
        // When & Then - should not throw
        assertDoesNotThrow(() -> TraceContext.clearTraceId());
        assertNull(TraceContext.getTraceId());
    }

    @Test @DisplayName("Generate trace ID should create unique IDs")
    void generateTraceId_ShouldCreateUniqueIds() {
        // When
        String traceId1 = TraceContext.generateTraceId();
        String traceId2 = TraceContext.generateTraceId();
        
        // Then
        assertNotNull(traceId1);
        assertNotNull(traceId2);
        assertNotEquals(traceId1, traceId2);
        assertEquals(32, traceId1.length());
        assertEquals(32, traceId2.length());
    }

    @Test @DisplayName("Get trace ID header should return correct header name")
    void getTraceIdHeader_ShouldReturnCorrectHeaderName() {
        // When
        String header = TraceContext.getTraceIdHeader();
        
        // Then
        assertEquals("X-Trace-ID", header);
    }

    @Test @DisplayName("Has trace ID should return true when trace ID is set")
    void hasTraceId_WhenSet_ShouldReturnTrue() {
        // Given
        TraceContext.setTraceId("test-trace-id");
        
        // When
        boolean hasTraceId = TraceContext.hasTraceId();
        
        // Then
        assertTrue(hasTraceId);
    }

    @Test @DisplayName("Has trace ID should return false when trace ID is not set")
    void hasTraceId_WhenNotSet_ShouldReturnFalse() {
        // When
        boolean hasTraceId = TraceContext.hasTraceId();
        
        // Then
        assertFalse(hasTraceId);
    }

    @Test @DisplayName("Has trace ID should return false after clearing")
    void hasTraceId_AfterClearing_ShouldReturnFalse() {
        // Given
        TraceContext.setTraceId("test-trace-id");
        assertTrue(TraceContext.hasTraceId());
        
        // When
        TraceContext.clearTraceId();
        
        // Then
        assertFalse(TraceContext.hasTraceId());
    }

    @Test @DisplayName("Multiple set operations should overwrite trace ID")
    void multipleSetOperations_ShouldOverwriteTraceId() {
        // Given
        TraceContext.setTraceId("first-trace-id");
        assertEquals("first-trace-id", TraceContext.getTraceId());
        
        // When
        TraceContext.setTraceId("second-trace-id");
        
        // Then
        assertEquals("second-trace-id", TraceContext.getTraceId());
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }

    private void assertDoesNotThrow(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new AssertionError("Expected no exception but got: " + e.getMessage(), e);
        }
    }
}
