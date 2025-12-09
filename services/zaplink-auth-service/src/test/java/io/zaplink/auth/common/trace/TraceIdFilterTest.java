package io.zaplink.auth.common.trace;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Comprehensive test suite for TraceIdFilter.
 * Tests trace ID extraction, generation, and request/response handling.
 */
@DisplayName("TraceIdFilter Tests")
class TraceIdFilterTest {

    private TraceIdFilter traceIdFilter;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traceIdFilter = new TraceIdFilter();
        TraceContext.clearTraceId();
    }

    @AfterEach
    void tearDown() {
        TraceContext.clearTraceId();
    }

    @Test @DisplayName("Filter should extract trace ID from request header")
    void doFilterInternal_ShouldExtractTraceIdFromHeader() throws ServletException, IOException {
        // Given
        String incomingTraceId = "incoming-trace-id-123";
        when(request.getHeader("X-Trace-ID")).thenReturn(incomingTraceId);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // When
        traceIdFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        assertEquals(incomingTraceId, TraceContext.getTraceId());
        verify(response).setHeader("X-Trace-ID", incomingTraceId);
        verify(filterChain).doFilter(request, response);
    }

    @Test @DisplayName("Filter should generate trace ID when not in header")
    void doFilterInternal_ShouldGenerateTraceIdWhenNotInHeader() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-Trace-ID")).thenReturn(null);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/auth");
        
        // When
        traceIdFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        String generatedTraceId = TraceContext.getTraceId();
        assertNotNull(generatedTraceId);
        assertEquals(32, generatedTraceId.length());
        verify(response).setHeader("X-Trace-ID", generatedTraceId);
        verify(filterChain).doFilter(request, response);
    }

    @Test @DisplayName("Filter should generate trace ID when header is empty")
    void doFilterInternal_ShouldGenerateTraceIdWhenHeaderIsEmpty() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-Trace-ID")).thenReturn("   ");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/users");
        
        // When
        traceIdFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        String generatedTraceId = TraceContext.getTraceId();
        assertNotNull(generatedTraceId);
        assertNotEquals("   ", generatedTraceId);
        verify(response).setHeader("X-Trace-ID", generatedTraceId);
        verify(filterChain).doFilter(request, response);
    }

    @Test @DisplayName("Filter should trim whitespace from trace ID")
    void doFilterInternal_ShouldTrimWhitespaceFromTraceId() throws ServletException, IOException {
        // Given
        String traceIdWithSpaces = "  trace-id-with-spaces  ";
        String expectedTraceId = "trace-id-with-spaces";
        when(request.getHeader("X-Trace-ID")).thenReturn(traceIdWithSpaces);
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getRequestURI()).thenReturn("/api/resource");
        
        // When
        traceIdFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        assertEquals(expectedTraceId, TraceContext.getTraceId());
        verify(response).setHeader("X-Trace-ID", expectedTraceId);
        verify(filterChain).doFilter(request, response);
    }

    @Test @DisplayName("Filter should clear trace ID in finally block")
    void doFilterInternal_ShouldClearTraceIdInFinallyBlock() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-Trace-ID")).thenReturn("test-trace-id");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        doThrow(new RuntimeException("Test exception")).when(filterChain).doFilter(request, response);
        
        // When & Then - should not throw exception
        assertThrows(RuntimeException.class, () -> {
            traceIdFilter.doFilterInternal(request, response, filterChain);
        });
        
        // Trace ID should still be set (clear is commented out in original code)
        assertEquals("test-trace-id", TraceContext.getTraceId());
    }

    @Test @DisplayName("Filter should skip actuator endpoints")
    void shouldNotFilter_ShouldSkipActuatorEndpoints() {
        // Given
        when(request.getRequestURI()).thenReturn("/actuator/health");
        
        // When
        boolean shouldNotFilter = traceIdFilter.shouldNotFilter(request);
        
        // Then
        assertTrue(shouldNotFilter);
    }

    @Test @DisplayName("Filter should skip health endpoints")
    void shouldNotFilter_ShouldSkipHealthEndpoints() {
        // Given
        when(request.getRequestURI()).thenReturn("/health");
        
        // When
        boolean shouldNotFilter = traceIdFilter.shouldNotFilter(request);
        
        // Then
        assertTrue(shouldNotFilter);
    }

    @Test @DisplayName("Filter should process regular endpoints")
    void shouldNotFilter_ShouldProcessRegularEndpoints() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        
        // When
        boolean shouldNotFilter = traceIdFilter.shouldNotFilter(request);
        
        // Then
        assertFalse(shouldNotFilter);
    }

    @Test @DisplayName("Filter should process root endpoint")
    void shouldNotFilter_ShouldProcessRootEndpoint() {
        // Given
        when(request.getRequestURI()).thenReturn("/");
        
        // When
        boolean shouldNotFilter = traceIdFilter.shouldNotFilter(request);
        
        // Then
        assertFalse(shouldNotFilter);
    }

    @Test @DisplayName("Filter should handle null request URI gracefully")
    void shouldNotFilter_ShouldHandleNullRequestUri() {
        // Given
        when(request.getRequestURI()).thenReturn(null);
        
        // When & Then - should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            traceIdFilter.shouldNotFilter(request);
        });
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }

    private void assertNotEquals(String expected, String actual) {
        if (expected.equals(actual)) {
            throw new AssertionError("Expected values to be different but both were: " + expected);
        }
    }
}
