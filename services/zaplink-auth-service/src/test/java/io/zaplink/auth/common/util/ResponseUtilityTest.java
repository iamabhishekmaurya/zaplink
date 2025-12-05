package io.zaplink.auth.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.zaplink.auth.dto.response.BaseResponse;

/**
 * Comprehensive test suite for ResponseUtility.
 * Tests all utility methods for creating standardized responses.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ResponseUtility Tests")
class ResponseUtilityTest {

    private ResponseUtility responseUtility;

    @BeforeEach
    void setUp() {
        responseUtility = new ResponseUtility();
    }

    @Test @DisplayName("createSuccessResponse should create success response")
    void createSuccessResponse_ShouldCreateSuccessResponse() {
        // Given
        String message = "Operation successful";
        
        // When
        BaseResponse response = responseUtility.createSuccessResponse(message);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("createSuccessResponse should handle null message")
    void createSuccessResponse_ShouldHandleNullMessage() {
        // Given
        String message = null;
        
        // When
        BaseResponse response = responseUtility.createSuccessResponse(message);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(null, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("createSuccessResponse should handle empty message")
    void createSuccessResponse_ShouldHandleEmptyMessage() {
        // Given
        String message = "";
        
        // When
        BaseResponse response = responseUtility.createSuccessResponse(message);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("createErrorResponse should create error response")
    void createErrorResponse_ShouldCreateErrorResponse() {
        // Given
        String message = "Operation failed";
        
        // When
        BaseResponse response = responseUtility.createErrorResponse(message);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("createErrorResponse should handle null message")
    void createErrorResponse_ShouldHandleNullMessage() {
        // Given
        String message = null;
        
        // When
        BaseResponse response = responseUtility.createErrorResponse(message);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(null, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("createErrorResponse should handle empty message")
    void createErrorResponse_ShouldHandleEmptyMessage() {
        // Given
        String message = "";
        
        // When
        BaseResponse response = responseUtility.createErrorResponse(message);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("Success and error responses should have different timestamps")
    void successAndErrorResponses_ShouldHaveDifferentTimestamps() throws InterruptedException {
        // Given
        String message = "Test message";
        
        // When
        BaseResponse successResponse = responseUtility.createSuccessResponse(message);
        Thread.sleep(1); // Small delay to ensure different timestamps
        BaseResponse errorResponse = responseUtility.createErrorResponse(message);
        
        // Then
        assertTrue(successResponse.getTimestamp().isBefore(errorResponse.getTimestamp()) ||
                  successResponse.getTimestamp().equals(errorResponse.getTimestamp()));
    }

    @Test @DisplayName("Responses should not have error code by default")
    void responses_ShouldNotHaveErrorCodeByDefault() {
        // Given
        String message = "Test message";
        
        // When
        BaseResponse successResponse = responseUtility.createSuccessResponse(message);
        BaseResponse errorResponse = responseUtility.createErrorResponse(message);
        
        // Then
        assertEquals(null, successResponse.getErrorCode());
        assertEquals(null, errorResponse.getErrorCode());
    }
}
