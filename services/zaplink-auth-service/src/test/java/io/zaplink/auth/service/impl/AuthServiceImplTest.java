package io.zaplink.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.zaplink.auth.common.exception.InvalidCredentialsException;
import io.zaplink.auth.common.util.JwtUtility;
import io.zaplink.auth.common.util.TokenUtility;
import io.zaplink.auth.dto.request.LoginRequest;
import io.zaplink.auth.dto.request.PasswordResetRequest;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.entity.RefreshToken;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.RefreshTokenRepository;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.service.helper.UserHelper;

/**
 * Comprehensive test suite for AuthServiceImpl.
 * Tests all authentication, token management, and password reset functionality.
 */
@ExtendWith(MockitoExtension.class) @DisplayName("AuthService Implementation Tests")
class AuthServiceImplTest
{
    @Mock
    private AuthenticationManager  authenticationManager;
    @Mock
    private UserRepository         userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder        passwordEncoder;
    @Mock
    private UserHelper             userHelper;
    @Mock
    private TokenUtility           tokenUtility;
    @Mock
    private JwtUtility             jwtUtility;
    @InjectMocks
    private AuthServiceImpl        authService;
    private User                   testUser;
    private LoginRequest           loginRequest;
    private RefreshToken           testRefreshToken;
    private PasswordResetRequest   passwordResetRequest;
    @BeforeEach
    void setUp()
    {
        testUser = User.builder().id( 1L ).email( "test@example.com" ).password( "encodedPassword" ).firstName( "Test" )
                .lastName( "User" ).active( true ).verified( true ).createdAt( Instant.now() ).build();
        loginRequest = new LoginRequest();
        loginRequest.setEmail( "test@example.com" );
        loginRequest.setPassword( "password123" );
        testRefreshToken = RefreshToken.builder().id( 1L ).token( "refresh-token-123" ).user( testUser )
                .expiryDate( Instant.now().plusSeconds( 3600 ) ).build();
        passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setEmail( "test@example.com" );
        // Only mock when needed in individual tests
    }

    @Test @DisplayName("Successful login should return JWT tokens")
    void login_SuccessfulLogin_ReturnsJwtTokens()
    {
        // Given
        when( userHelper.findUserByEmailOrThrow( eq( "test@example.com" ), anyString() ) ).thenReturn( testUser );
        when( jwtUtility.generateAccessToken( testUser ) ).thenReturn( "access-token" );
        when( tokenUtility.generateToken() ).thenReturn( "refresh-token" );
        when( tokenUtility.generateRefreshTokenExpiry() ).thenReturn( Instant.now().plusSeconds( 3600 ) );
        when( refreshTokenRepository.save( any( RefreshToken.class ) ) ).thenReturn( testRefreshToken );
        when( jwtUtility.buildLoginResponse( eq( testUser ), eq( "access-token" ), eq( "refresh-token" ) ) )
                .thenReturn( LoginResponse.builder().accessToken( "access-token" ).refreshToken( "refresh-token" )
                        .build() );
        // When
        LoginResponse response = authService.login( loginRequest );
        // Then
        assertNotNull( response );
        assertEquals( "access-token", response.getAccessToken() );
        assertEquals( "refresh-token", response.getRefreshToken() );
        verify( authenticationManager ).authenticate( any( UsernamePasswordAuthenticationToken.class ) );
        verify( userHelper ).findUserByEmailOrThrow( "test@example.com", "authentication" );
        verify( userHelper ).validateUserActive( testUser, "login" );
        verify( jwtUtility ).generateAccessToken( testUser );
        verify( refreshTokenRepository ).save( any( RefreshToken.class ) );
    }

    @Test @DisplayName("Login with invalid credentials should throw InvalidCredentialsException")
    void login_InvalidCredentials_ThrowsInvalidCredentialsException()
    {
        // Given
        doThrow( new RuntimeException( "Authentication failed" ) ).when( authenticationManager )
                .authenticate( any( UsernamePasswordAuthenticationToken.class ) );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class,
                                                              () -> authService.login( loginRequest ) );
        assertNotNull( exception );
        verify( authenticationManager ).authenticate( any( UsernamePasswordAuthenticationToken.class ) );
    }

    @Test @DisplayName("Login with inactive user should throw InvalidCredentialsException")
    void login_InactiveUser_ThrowsInvalidCredentialsException()
    {
        // Given
        testUser.setActive( false );
        when( userHelper.findUserByEmailOrThrow( eq( "test@example.com" ), anyString() ) ).thenReturn( testUser );
        doThrow( new IllegalArgumentException( "User account deactivated" ) ).when( userHelper )
                .validateUserActive( testUser, "login" );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class,
                                                              () -> authService.login( loginRequest ) );
        assertNotNull( exception );
        verify( authenticationManager ).authenticate( any( UsernamePasswordAuthenticationToken.class ) );
        verify( userHelper ).findUserByEmailOrThrow( "test@example.com", "authentication" );
        verify( userHelper ).validateUserActive( testUser, "login" );
    }

    @Test @DisplayName("Refresh token with valid token should return new tokens")
    void refreshToken_ValidToken_ReturnsNewTokens()
    {
        // Given
        when( refreshTokenRepository.findByToken( "refresh-token-123" ) ).thenReturn( Optional.of( testRefreshToken ) );
        when( tokenUtility.generateToken() ).thenReturn( "new-refresh-token" );
        when( tokenUtility.generateRefreshTokenExpiry() ).thenReturn( Instant.now().plusSeconds( 3600 ) );
        when( jwtUtility.generateAccessToken( testUser ) ).thenReturn( "new-access-token" );
        when( jwtUtility.buildLoginResponse( eq( testUser ), eq( "new-access-token" ), eq( "new-refresh-token" ) ) )
                .thenReturn( LoginResponse.builder().accessToken( "new-access-token" )
                        .refreshToken( "new-refresh-token" ).build() );
        // When
        LoginResponse response = authService.refreshToken( "refresh-token-123" );
        // Then
        assertNotNull( response );
        assertEquals( "new-access-token", response.getAccessToken() );
        assertEquals( "new-refresh-token", response.getRefreshToken() );
        verify( refreshTokenRepository ).findByToken( "refresh-token-123" );
        verify( refreshTokenRepository ).delete( testRefreshToken );
        verify( refreshTokenRepository ).save( any( RefreshToken.class ) );
        verify( jwtUtility ).generateAccessToken( testUser );
    }

    @Test @DisplayName("Refresh token with invalid token should throw InvalidCredentialsException")
    void refreshToken_InvalidToken_ThrowsInvalidCredentialsException()
    {
        // Given
        when( refreshTokenRepository.findByToken( "invalid-token" ) ).thenReturn( Optional.empty() );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class,
                                                              () -> authService.refreshToken( "invalid-token" ) );
        assertNotNull( exception );
        verify( refreshTokenRepository ).findByToken( "invalid-token" );
    }

    @Test @DisplayName("Refresh token with expired token should throw InvalidCredentialsException")
    void refreshToken_ExpiredToken_ThrowsInvalidCredentialsException()
    {
        // Given
        testRefreshToken.setExpiryDate( Instant.now().minusSeconds( 1 ) ); // Expired
        when( refreshTokenRepository.findByToken( "expired-token" ) ).thenReturn( Optional.of( testRefreshToken ) );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class,
                                                              () -> authService.refreshToken( "expired-token" ) );
        assertNotNull( exception );
        verify( refreshTokenRepository ).findByToken( "expired-token" );
        verify( refreshTokenRepository ).delete( testRefreshToken );
    }

    @Test @DisplayName("Logout with valid token should succeed")
    void logout_ValidToken_Succeeds()
    {
        // Given
        when( refreshTokenRepository.findByToken( "refresh-token-123" ) ).thenReturn( Optional.of( testRefreshToken ) );
        // When
        assertDoesNotThrow( () -> authService.logout( "refresh-token-123" ) );
        // Then
        verify( refreshTokenRepository ).findByToken( "refresh-token-123" );
        verify( refreshTokenRepository ).delete( testRefreshToken );
    }

    @Test @DisplayName("Logout with invalid token should throw InvalidCredentialsException")
    void logout_InvalidToken_ThrowsInvalidCredentialsException()
    {
        // Given
        when( refreshTokenRepository.findByToken( "invalid-token" ) ).thenReturn( Optional.empty() );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class,
                                                              () -> authService.logout( "invalid-token" ) );
        assertNotNull( exception );
        verify( refreshTokenRepository ).findByToken( "invalid-token" );
    }

    @Test @DisplayName("Request password reset with existing user should succeed")
    void requestPasswordReset_ExistingUser_Succeeds()
    {
        // Given
        when( userHelper.findUserByEmailOrThrow( "test@example.com", "password reset" ) ).thenReturn( testUser );
        when( tokenUtility.generateToken() ).thenReturn( "reset-token" );
        when( tokenUtility.generatePasswordResetExpiry() ).thenReturn( Instant.now().plusSeconds( 3600 ) );
        // When
        assertDoesNotThrow( () -> authService.requestPasswordReset( passwordResetRequest ) );
        // Then
        verify( userHelper ).findUserByEmailOrThrow( "test@example.com", "password reset" );
        verify( tokenUtility ).generateToken();
        verify( tokenUtility ).generatePasswordResetExpiry();
        verify( userHelper ).updateUser( eq( testUser ), any() );
    }

    @Test @DisplayName("Reset password with valid token should succeed")
    void resetPassword_ValidToken_Succeeds()
    {
        // Given
        testUser.setResetToken( "valid-reset-token" );
        testUser.setResetTokenExpiry( Instant.now().plusSeconds( 3600 ) );
        when( userRepository.findByResetToken( "valid-reset-token" ) ).thenReturn( Optional.of( testUser ) );
        when( tokenUtility.isTokenExpired( any() ) ).thenReturn( false );
        // When
        assertDoesNotThrow( () -> authService.resetPassword( "valid-reset-token", "newPassword" ) );
        // Then
        verify( userRepository ).findByResetToken( "valid-reset-token" );
        verify( tokenUtility ).isTokenExpired( any() );
        verify( userHelper ).updateUser( eq( testUser ), any() );
        verify( refreshTokenRepository ).deleteByUser( testUser );
    }

    @Test @DisplayName("Reset password with invalid token should throw InvalidCredentialsException")
    void resetPassword_InvalidToken_ThrowsInvalidCredentialsException()
    {
        // Given
        when( userRepository.findByResetToken( "invalid-token" ) ).thenReturn( Optional.empty() );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class, () -> authService
                .resetPassword( "invalid-token", "newPassword" ) );
        assertNotNull( exception );
        verify( userRepository ).findByResetToken( "invalid-token" );
    }

    @Test @DisplayName("Reset password with expired token should throw InvalidCredentialsException")
    void resetPassword_ExpiredToken_ThrowsInvalidCredentialsException()
    {
        // Given
        testUser.setResetToken( "expired-token" );
        testUser.setResetTokenExpiry( Instant.now().minusSeconds( 1 ) ); // Expired
        when( userRepository.findByResetToken( "expired-token" ) ).thenReturn( Optional.of( testUser ) );
        when( tokenUtility.isTokenExpired( any() ) ).thenReturn( true );
        // When & Then
        InvalidCredentialsException exception = assertThrows( InvalidCredentialsException.class, () -> authService
                .resetPassword( "expired-token", "newPassword" ) );
        assertNotNull( exception );
        verify( userRepository ).findByResetToken( "expired-token" );
        verify( tokenUtility ).isTokenExpired( any() );
    }

    @Test @DisplayName("Generate and save refresh token should return token string")
    void generateAndSaveRefreshToken_ValidUser_ReturnsTokenString()
        throws Exception
    {
        // Given
        when( tokenUtility.generateToken() ).thenReturn( "new-refresh-token" );
        when( tokenUtility.generateRefreshTokenExpiry() ).thenReturn( Instant.now().plusSeconds( 3600 ) );
        when( refreshTokenRepository.save( any( RefreshToken.class ) ) ).thenReturn( testRefreshToken );
        // Use reflection to test private method
        java.lang.reflect.Method method = AuthServiceImpl.class.getDeclaredMethod( "generateAndSaveRefreshToken",
                                                                                   User.class );
        method.setAccessible( true );
        // When
        String result = (String) method.invoke( authService, testUser );
        // Then
        assertEquals( "new-refresh-token", result );
        verify( tokenUtility ).generateToken();
        verify( tokenUtility ).generateRefreshTokenExpiry();
        verify( refreshTokenRepository ).save( any( RefreshToken.class ) );
    }
}
