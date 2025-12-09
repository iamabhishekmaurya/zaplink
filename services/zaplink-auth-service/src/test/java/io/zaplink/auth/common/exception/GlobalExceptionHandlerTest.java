package io.zaplink.auth.common.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.dto.response.BaseResponse;

/**
 * Comprehensive test suite for GlobalExceptionHandler.
 * Tests all exception handling methods and response generation.
 */
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    
    @Mock
    private AuthException authException;
    
    @Mock
    private UserAlreadyExistsException userAlreadyExistsException;
    
    @Mock
    private UserNotFoundException userNotFoundException;
    
    @Mock
    private InvalidCredentialsException invalidCredentialsException;
    
    @Mock
    private AuthenticationException authenticationException;
    
    @Mock
    private BadCredentialsException badCredentialsException;
    
    @Mock
    private AccessDeniedException accessDeniedException;
    
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    
    @Mock
    private BindingResult bindingResult;
    
    @Mock
    private FieldError fieldError;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @AfterEach
    void tearDown() {
        // Clean up if needed
    }

    @Test @DisplayName("Handle AuthException should return unauthorized response")
    void handleAuthException_ShouldReturnUnauthorizedResponse() {
        // Given
        String message = "Authentication failed";
        String errorCode = "AUTH_ERROR";
        when(authException.getMessage()).thenReturn(message);
        when(authException.getErrorCode()).thenReturn(errorCode);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleAuthException(authException);
        
        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(message, body.getMessage());
        assertEquals(errorCode, body.getErrorCode());
    }

    @Test @DisplayName("Handle UserAlreadyExistsException should return conflict response")
    void handleUserAlreadyExistsException_ShouldReturnConflictResponse() {
        // Given
        String message = "User already exists";
        String errorCode = "USER_EXISTS";
        when(userAlreadyExistsException.getMessage()).thenReturn(message);
        when(userAlreadyExistsException.getErrorCode()).thenReturn(errorCode);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleUserAlreadyExistsException(userAlreadyExistsException);
        
        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(message, body.getMessage());
        assertEquals(errorCode, body.getErrorCode());
    }

    @Test @DisplayName("Handle UserNotFoundException should return not found response")
    void handleUserNotFoundException_ShouldReturnNotFoundResponse() {
        // Given
        String message = "User not found";
        String errorCode = "USER_NOT_FOUND";
        when(userNotFoundException.getMessage()).thenReturn(message);
        when(userNotFoundException.getErrorCode()).thenReturn(errorCode);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleUserNotFoundException(userNotFoundException);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(message, body.getMessage());
        assertEquals(errorCode, body.getErrorCode());
    }

    @Test @DisplayName("Handle InvalidCredentialsException should return unauthorized response")
    void handleInvalidCredentialsException_ShouldReturnUnauthorizedResponse() {
        // Given
        String message = "Invalid credentials";
        String errorCode = "INVALID_CREDENTIALS";
        when(invalidCredentialsException.getMessage()).thenReturn(message);
        when(invalidCredentialsException.getErrorCode()).thenReturn(errorCode);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleInvalidCredentialsException(invalidCredentialsException);
        
        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(message, body.getMessage());
        assertEquals(errorCode, body.getErrorCode());
    }

    @Test @DisplayName("Handle AuthenticationException should return unauthorized response")
    void handleAuthenticationException_ShouldReturnUnauthorizedResponse() {
        // Given
        String message = "Authentication failed";
        when(authenticationException.getMessage()).thenReturn(message);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleAuthenticationException(authenticationException);
        
        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_AUTHENTICATION_FAILED, body.getMessage());
        assertEquals(ApiConstants.ERROR_AUTHENTICATION_FAILED, body.getErrorCode());
    }

    @Test @DisplayName("Handle BadCredentialsException should return unauthorized response")
    void handleBadCredentialsException_ShouldReturnUnauthorizedResponse() {
        // Given
        String message = "Bad credentials";
        when(badCredentialsException.getMessage()).thenReturn(message);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleBadCredentialsException(badCredentialsException);
        
        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_INVALID_EMAIL_OR_PASSWORD, body.getMessage());
        assertEquals(ApiConstants.ERROR_INVALID_CREDENTIALS, body.getErrorCode());
    }

    @Test @DisplayName("Handle AccessDeniedException should return forbidden response")
    void handleAccessDeniedException_ShouldReturnForbiddenResponse() {
        // Given
        String message = "Access denied";
        when(accessDeniedException.getMessage()).thenReturn(message);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleAccessDeniedException(accessDeniedException);
        
        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_ACCESS_DENIED, body.getMessage());
        assertEquals(ApiConstants.ERROR_ACCESS_DENIED, body.getErrorCode());
    }

    @Test @DisplayName("Handle MethodArgumentNotValidException should return bad request response")
    void handleValidationExceptions_ShouldReturnBadRequestResponse() {
        // Given
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.List.of(fieldError));
        when(fieldError.getField()).thenReturn("email");
        when(fieldError.getDefaultMessage()).thenReturn("Invalid email format");
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);
        
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_VALIDATION_FAILED, body.getMessage());
        assertEquals(ApiConstants.ERROR_VALIDATION_ERROR, body.getErrorCode());
    }

    @Test @DisplayName("Handle global Exception should return internal server error response")
    void handleGlobalException_ShouldReturnInternalServerErrorResponse() {
        // Given
        String message = "Unexpected error";
        Exception exception = new RuntimeException(message);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleGlobalException(exception);
        
        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_UNEXPECTED_ERROR, body.getMessage());
        assertEquals(ApiConstants.ERROR_INTERNAL_SERVER_ERROR, body.getErrorCode());
    }

    @Test @DisplayName("Handle null exception message gracefully")
    void handleGlobalException_ShouldHandleNullMessageGracefully() {
        // Given
        Exception exception = new RuntimeException((String) null);
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleGlobalException(exception);
        
        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_UNEXPECTED_ERROR, body.getMessage());
        assertEquals(ApiConstants.ERROR_INTERNAL_SERVER_ERROR, body.getErrorCode());
    }

    @Test @DisplayName("Handle validation exceptions with multiple field errors")
    void handleValidationExceptions_ShouldHandleMultipleFieldErrors() {
        // Given
        FieldError fieldError1 = mock(FieldError.class);
        FieldError fieldError2 = mock(FieldError.class);
        
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.List.of(fieldError1, fieldError2));
        when(fieldError1.getField()).thenReturn("email");
        when(fieldError1.getDefaultMessage()).thenReturn("Invalid email");
        when(fieldError2.getField()).thenReturn("password");
        when(fieldError2.getDefaultMessage()).thenReturn("Password too short");
        
        // When
        ResponseEntity<BaseResponse> response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);
        
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        BaseResponse body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(ApiConstants.MESSAGE_VALIDATION_FAILED, body.getMessage());
        assertEquals(ApiConstants.ERROR_VALIDATION_ERROR, body.getErrorCode());
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }
}
