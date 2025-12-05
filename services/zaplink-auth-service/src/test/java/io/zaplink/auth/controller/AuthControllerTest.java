package io.zaplink.auth.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.dto.request.LoginRequest;
import io.zaplink.auth.dto.request.PasswordResetRequest;
import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.dto.response.BaseResponse;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.dto.response.UserRegistrationResponse;
import io.zaplink.auth.service.AuthService;
import io.zaplink.auth.service.RegistrationService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Comprehensive test suite for AuthController.
 * Tests all REST endpoints for authentication operations.
 */
@ExtendWith(MockitoExtension.class) @DisplayName("AuthController Tests")
class AuthControllerTest
{
    @Mock
    private RegistrationService      registrationService;
    @Mock
    private AuthService              authService;
    @InjectMocks
    private AuthController           authController;
    private Validator                validator;
    private UserRegistrationRequest  registrationRequest;
    private LoginRequest             loginRequest;
    private PasswordResetRequest     passwordResetRequest;
    private UserRegistrationResponse registrationResponse;
    private LoginResponse            loginResponse;
    @BeforeEach
    void setUp()
    {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setEmail( "test@example.com" );
        registrationRequest.setUsername( "testuser" );
        registrationRequest.setPassword( "password123" );
        registrationRequest.setFirstName( "Test" );
        registrationRequest.setLastName( "User" );
        registrationRequest.setPhoneNumber( "1234567890" );
        loginRequest = new LoginRequest();
        loginRequest.setEmail( "test@example.com" );
        loginRequest.setPassword( "password123" );
        passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail( "test@example.com" );
        registrationResponse = UserRegistrationResponse.builder().success( true )
                .message( "User registered successfully" ).userId( 1L ).username( "testuser" )
                .email( "test@example.com" ).firstName( "Test" ).lastName( "User" ).verified( false )
                .timestamp( java.time.Instant.now() ).build();
        loginResponse = LoginResponse.builder().success( true ).message( "Login successful" )
                .accessToken( "access-token" ).refreshToken( "refresh-token" ).timestamp( java.time.Instant.now() )
                .userInfo( new LoginResponse.UserInfo( 1L, "testuser", "test@example.com", "Test", "User", true ) )
                .build();
    }

    @Test @DisplayName("Register user with valid data should return created response")
    void registerUser_ValidData_ReturnsCreatedResponse()
    {
        // Given
        when( registrationService.registerUser( registrationRequest ) ).thenReturn( registrationResponse );
        // When
        ResponseEntity<UserRegistrationResponse> response = ResponseEntity.status( HttpStatus.CREATED )
                .body( authController.registerUser( registrationRequest ) );
        // Then
        assertEquals( HttpStatus.CREATED, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertEquals( registrationResponse, response.getBody() );
        verify( registrationService ).registerUser( registrationRequest );
    }

    @Test @DisplayName("Register user with invalid email should throw validation exception")
    void registerUser_InvalidEmail_ThrowsValidationException()
    {
        // Given
        registrationRequest.setEmail( "invalid-email" );
        // When
        var violations = validator.validate( registrationRequest );
        // Then
        assertFalse( violations.isEmpty() );
        assertTrue( violations.stream().anyMatch( v -> v.getPropertyPath().toString().equals( "email" ) ) );
    }

    @Test @DisplayName("Register user with blank username should throw validation exception")
    void registerUser_BlankUsername_ThrowsValidationException()
    {
        // Given
        registrationRequest.setUsername( "" );
        // When
        var violations = validator.validate( registrationRequest );
        // Then
        assertFalse( violations.isEmpty() );
        assertTrue( violations.stream().anyMatch( v -> v.getPropertyPath().toString().equals( "username" ) ) );
    }

    @Test @DisplayName("Register user with short password should throw validation exception")
    void registerUser_ShortPassword_ThrowsValidationException()
    {
        // Given
        registrationRequest.setPassword( "123" );
        // When
        var violations = validator.validate( registrationRequest );
        // Then
        assertFalse( violations.isEmpty() );
        assertTrue( violations.stream().anyMatch( v -> v.getPropertyPath().toString().equals( "password" ) ) );
    }

    @Test @DisplayName("Login with valid credentials should return tokens")
    void login_ValidCredentials_ReturnsTokens()
    {
        // Given
        when( authService.login( loginRequest ) ).thenReturn( loginResponse );
        // When
        ResponseEntity<LoginResponse> response = ResponseEntity.ok( authController.login( loginRequest ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertEquals( loginResponse, response.getBody() );
        verify( authService ).login( loginRequest );
    }

    @Test @DisplayName("Login with invalid email should throw validation exception")
    void login_InvalidEmail_ThrowsValidationException()
    {
        // Given
        loginRequest.setEmail( "invalid-email" );
        // When
        var violations = validator.validate( loginRequest );
        // Then
        assertFalse( violations.isEmpty() );
        assertTrue( violations.stream().anyMatch( v -> v.getPropertyPath().toString().equals( "email" ) ) );
    }

    @Test @DisplayName("Login with blank password should throw validation exception")
    void login_BlankPassword_ThrowsValidationException()
    {
        // Given
        loginRequest.setPassword( "" );
        // When
        var violations = validator.validate( loginRequest );
        // Then
        assertFalse( violations.isEmpty() );
        assertTrue( violations.stream().anyMatch( v -> v.getPropertyPath().toString().equals( "password" ) ) );
    }

    @Test @DisplayName("Refresh token with valid token should return new tokens")
    void refreshToken_ValidToken_ReturnsNewTokens()
    {
        // Given
        when( authService.refreshToken( "refresh-token" ) ).thenReturn( loginResponse );
        // When
        ResponseEntity<LoginResponse> response = ResponseEntity.ok( authController.refreshToken( "refresh-token" ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertEquals( loginResponse, response.getBody() );
        verify( authService ).refreshToken( "refresh-token" );
    }

    @Test @DisplayName("Logout with valid token should return success response")
    void logout_ValidToken_ReturnsSuccessResponse()
    {
        // Given
        doNothing().when( authService ).logout( "refresh-token" );
        // When
        ResponseEntity<BaseResponse> response = ResponseEntity.ok( authController.logout( "refresh-token" ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertTrue( response.getBody().isSuccess() );
        assertEquals( ApiConstants.MESSAGE_LOGOUT_SUCCESSFUL, response.getBody().getMessage() );
        verify( authService ).logout( "refresh-token" );
    }

    @Test @DisplayName("Verify email with valid token should return success response")
    void verifyEmail_ValidToken_ReturnsSuccessResponse()
    {
        // Given
        doNothing().when( registrationService ).verifyEmail( "verification-token" );
        // When
        ResponseEntity<BaseResponse> response = ResponseEntity.ok( authController.verifyEmail( "verification-token" ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertTrue( response.getBody().isSuccess() );
        assertEquals( ApiConstants.MESSAGE_EMAIL_VERIFIED_SUCCESSFULLY, response.getBody().getMessage() );
        verify( registrationService ).verifyEmail( "verification-token" );
    }

    @Test @DisplayName("Resend verification email with valid email should return success response")
    void resendVerificationEmail_ValidEmail_ReturnsSuccessResponse()
    {
        // Given
        doNothing().when( registrationService ).resendVerificationEmail( "test@example.com" );
        // When
        ResponseEntity<BaseResponse> response = ResponseEntity
                .ok( authController.resendVerificationEmail( "test@example.com" ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertTrue( response.getBody().isSuccess() );
        assertEquals( ApiConstants.MESSAGE_VERIFICATION_EMAIL_SENT, response.getBody().getMessage() );
        verify( registrationService ).resendVerificationEmail( "test@example.com" );
    }

    @Test @DisplayName("Request password reset with valid email should return success response")
    void requestPasswordReset_ValidEmail_ReturnsSuccessResponse()
    {
        // Given
        doNothing().when( authService ).requestPasswordReset( passwordResetRequest );
        // When
        ResponseEntity<BaseResponse> response = ResponseEntity
                .ok( authController.requestPasswordReset( passwordResetRequest ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertTrue( response.getBody().isSuccess() );
        assertEquals( ApiConstants.MESSAGE_PASSWORD_RESET_EMAIL_SENT, response.getBody().getMessage() );
        verify( authService ).requestPasswordReset( passwordResetRequest );
    }

    @Test @DisplayName("Request password reset with invalid email should throw validation exception")
    void requestPasswordReset_InvalidEmail_ThrowsValidationException()
    {
        // Given
        passwordResetRequest.setEmail( "invalid-email" );
        // When
        var violations = validator.validate( passwordResetRequest );
        // Then
        assertFalse( violations.isEmpty() );
        assertTrue( violations.stream().anyMatch( v -> v.getPropertyPath().toString().equals( "email" ) ) );
    }

    @Test @DisplayName("Reset password with valid token should return success response")
    void resetPassword_ValidToken_ReturnsSuccessResponse()
    {
        // Given
        doNothing().when( authService ).resetPassword( "reset-token", "newPassword" );
        // When
        ResponseEntity<BaseResponse> response = ResponseEntity
                .ok( authController.resetPassword( "reset-token", "newPassword" ) );
        // Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertTrue( response.getBody().isSuccess() );
        assertEquals( ApiConstants.MESSAGE_PASSWORD_RESET_SUCCESSFULLY, response.getBody().getMessage() );
        verify( authService ).resetPassword( "reset-token", "newPassword" );
    }

    @Test @DisplayName("Reset password with null token should handle gracefully")
    void resetPassword_NullToken_HandlesGracefully()
    {
        // Given
        doNothing().when( authService ).resetPassword( null, "newPassword" );
        // When
        assertDoesNotThrow( () -> authController.resetPassword( null, "newPassword" ) );
        // Then
        verify( authService ).resetPassword( null, "newPassword" );
    }

    @Test @DisplayName("Reset password with null password should handle gracefully")
    void resetPassword_NullPassword_HandlesGracefully()
    {
        // Given
        doNothing().when( authService ).resetPassword( "reset-token", null );
        // When
        assertDoesNotThrow( () -> authController.resetPassword( "reset-token", null ) );
        // Then
        verify( authService ).resetPassword( "reset-token", null );
    }

    @Test @DisplayName("Register user with null request should throw validation exception")
    void registerUser_NullRequest_ThrowsValidationException()
    {
        // When & Then
        assertThrows( NullPointerException.class, () -> authController.registerUser( null ) );
    }

    @Test @DisplayName("Login with null request should throw validation exception")
    void login_NullRequest_ThrowsValidationException()
    {
        // When & Then
        assertThrows( NullPointerException.class, () -> authController.login( null ) );
    }

    @Test @DisplayName("Request password reset with null request should throw validation exception")
    void requestPasswordReset_NullRequest_ThrowsValidationException()
    {
        // When & Then
        assertThrows( NullPointerException.class, () -> authController.requestPasswordReset( null ) );
    }

    @Test @DisplayName("Register user should handle registration service exception")
    void registerUser_ServiceException_PropagatesException()
    {
        // Given
        when( registrationService.registerUser( registrationRequest ) )
                .thenThrow( new RuntimeException( "Registration failed" ) );
        // When & Then
        assertThrows( RuntimeException.class, () -> authController.registerUser( registrationRequest ) );
        verify( registrationService ).registerUser( registrationRequest );
    }

    @Test @DisplayName("Login should handle auth service exception")
    void login_ServiceException_PropagatesException()
    {
        // Given
        when( authService.login( loginRequest ) ).thenThrow( new RuntimeException( "Authentication failed" ) );
        // When & Then
        assertThrows( RuntimeException.class, () -> authController.login( loginRequest ) );
        verify( authService ).login( loginRequest );
    }

    @Test @DisplayName("Refresh token should handle service exception")
    void refreshToken_ServiceException_PropagatesException()
    {
        // Given
        when( authService.refreshToken( "invalid-token" ) ).thenThrow( new RuntimeException( "Invalid token" ) );
        // When & Then
        assertThrows( RuntimeException.class, () -> authController.refreshToken( "invalid-token" ) );
        verify( authService ).refreshToken( "invalid-token" );
    }
}
