package io.zaplink.auth.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for BaseResponse.
 * Tests all constructors, static factory methods, and builder patterns.
 */
@DisplayName("BaseResponse Tests")
class BaseResponseTest {

    @Test @DisplayName("Default constructor should create empty response")
    void defaultConstructor_ShouldCreateEmptyResponse() {
        // When
        BaseResponse response = new BaseResponse();
        
        // Then
        assertNotNull(response);
    }

    @Test @DisplayName("AllArgsConstructor should create response with all fields")
    void allArgsConstructor_ShouldCreateResponseWithAllFields() {
        // Given
        boolean success = true;
        String message = "Test message";
        String errorCode = "ERR001";
        Instant timestamp = Instant.now();
        
        // When
        BaseResponse response = new BaseResponse(success, message, errorCode, timestamp);
        
        // Then
        assertEquals(success, response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test @DisplayName("Constructor with success and message should set timestamp")
    void constructorWithSuccessAndMessage_ShouldSetTimestamp() {
        // Given
        boolean success = true;
        String message = "Success message";
        
        // When
        BaseResponse response = new BaseResponse(success, message);
        
        // Then
        assertEquals(success, response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("Constructor with success, message, and errorCode should set timestamp")
    void constructorWithSuccessMessageAndErrorCode_ShouldSetTimestamp() {
        // Given
        boolean success = false;
        String message = "Error message";
        String errorCode = "ERR001";
        
        // When
        BaseResponse response = new BaseResponse(success, message, errorCode);
        
        // Then
        assertEquals(success, response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(errorCode, response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("Static success method should create success response")
    void staticSuccessMethod_ShouldCreateSuccessResponse() {
        // Given
        String message = "Operation successful";
        
        // When
        BaseResponse response = BaseResponse.success(message);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("Static error method should create error response")
    void staticErrorMethod_ShouldCreateErrorResponse() {
        // Given
        String message = "Operation failed";
        
        // When
        BaseResponse response = BaseResponse.error(message);
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("Static error method with errorCode should create error response with code")
    void staticErrorMethodWithErrorCode_ShouldCreateErrorResponseWithCode() {
        // Given
        String message = "Operation failed";
        String errorCode = "ERR001";
        
        // When
        BaseResponse response = BaseResponse.error(message, errorCode);
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(errorCode, response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test @DisplayName("Builder pattern should create response with all fields")
    void builderPattern_ShouldCreateResponseWithAllFields() {
        // Given
        Instant timestamp = Instant.now();
        
        // When
        BaseResponse response = BaseResponse.builder()
                .success(true)
                .message("Builder message")
                .errorCode("ERR001")
                .timestamp(timestamp)
                .build();
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("Builder message", response.getMessage());
        assertEquals("ERR001", response.getErrorCode());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        BaseResponse response = new BaseResponse();
        Instant timestamp = Instant.now();
        
        // When
        response.setSuccess(true);
        response.setMessage("Updated message");
        response.setErrorCode("ERR002");
        response.setTimestamp(timestamp);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("Updated message", response.getMessage());
        assertEquals("ERR002", response.getErrorCode());
        assertEquals(timestamp, response.getTimestamp());
    }
}
