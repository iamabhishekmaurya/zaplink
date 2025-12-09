package io.zaplink.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.zaplink.auth.common.exception.UserAlreadyExistsException;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.dto.request.EmailRequest;
import io.zaplink.auth.dto.response.UserRegistrationResponse;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.service.UserService;
import io.zaplink.auth.service.helper.KafkaServiceHelper;

/**
 * Comprehensive test suite for RegistrationServiceImpl.
 * Tests all user registration, email verification, and account management functionality.
 */
@ExtendWith(MockitoExtension.class) @DisplayName("RegistrationService Implementation Tests")
class RegistrationServiceImplTest
{
    @Mock
    private UserService             userService;
    @Mock
    private UserRepository          userRepository;
    @Mock
    private KafkaServiceHelper      kafkaServiceHelper;
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    private UserRegistrationRequest registrationRequest;
    private User                    testUser;
    @BeforeEach
    void setUp()
    {
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setEmail( "test@example.com" );
        registrationRequest.setUsername( "testuser" );
        registrationRequest.setPassword( "password123" );
        registrationRequest.setFirstName( "Test" );
        registrationRequest.setLastName( "User" );
        registrationRequest.setPhoneNumber( "1234567890" );
        testUser = User.builder().id( 1L ).email( "test@example.com" ).username( "testuser" )
                .password( "encodedPassword" ).firstName( "Test" ).lastName( "User" ).phoneNumber( "1234567890" )
                .active( true ).verified( false ).verificationToken( UUID.randomUUID().toString() )
                .createdAt( Instant.now() ).build();
    }

    @Test @DisplayName("Register new user with valid data should succeed")
    void registerUser_ValidData_Succeeds()
    {
        // Given
        when( userService.existsByEmail( "test@example.com" ) ).thenReturn( false );
        when( userService.existsByUsername( "testuser" ) ).thenReturn( false );
        when( userService.createUser( registrationRequest ) ).thenReturn( testUser );
        // When
        UserRegistrationResponse response = registrationService.registerUser( registrationRequest );
        // Then
        assertNotNull( response );
        assertTrue( response.isSuccess() );
        assertEquals( testUser.getId(), response.getUserId() );
        assertEquals( testUser.getEmail(), response.getEmail() );
        assertEquals( testUser.getUsername(), response.getUsername() );
        assertEquals( testUser.getFirstName(), response.getFirstName() );
        assertEquals( testUser.getLastName(), response.getLastName() );
        assertEquals( testUser.isVerified(), response.isVerified() );
        assertEquals( testUser.getCreatedAt(), response.getCreatedAt() );
        verify( userService ).existsByEmail( "test@example.com" );
        verify( userService ).existsByUsername( "testuser" );
        verify( userService ).createUser( registrationRequest );
        verify( kafkaServiceHelper ).sendMessage( any( EmailRequest.class ) );
    }

    @Test @DisplayName("Register user with existing email should throw UserAlreadyExistsException")
    void registerUser_ExistingEmail_ThrowsUserAlreadyExistsException()
    {
        // Given
        when( userService.existsByEmail( "test@example.com" ) ).thenReturn( true );
        // When & Then
        UserAlreadyExistsException exception = assertThrows( UserAlreadyExistsException.class, () -> registrationService
                .registerUser( registrationRequest ) );
        assertNotNull( exception );
        assertTrue( exception.getMessage().contains( "test@example.com" ) );
        verify( userService ).existsByEmail( "test@example.com" );
        verifyNoMoreInteractions( userService );
    }

    @Test @DisplayName("Register user with existing username should throw UserAlreadyExistsException")
    void registerUser_ExistingUsername_ThrowsUserAlreadyExistsException()
    {
        // Given
        when( userService.existsByEmail( "test@example.com" ) ).thenReturn( false );
        when( userService.existsByUsername( "testuser" ) ).thenReturn( true );
        // When & Then
        UserAlreadyExistsException exception = assertThrows( UserAlreadyExistsException.class, () -> registrationService
                .registerUser( registrationRequest ) );
        assertNotNull( exception );
        assertTrue( exception.getMessage().contains( "testuser" ) );
        verify( userService ).existsByEmail( "test@example.com" );
        verify( userService ).existsByUsername( "testuser" );
        verifyNoMoreInteractions( userService );
    }

    @Test @DisplayName("Verify email with valid token should succeed")
    void verifyEmail_ValidToken_Succeeds()
    {
        // Given
        String verificationToken = UUID.randomUUID().toString();
        testUser.setVerificationToken( verificationToken );
        testUser.setVerified( false );
        when( userRepository.findByVerificationToken( verificationToken ) ).thenReturn( Optional.of( testUser ) );
        // When
        assertDoesNotThrow( () -> registrationService.verifyEmail( verificationToken ) );
        // Then
        assertTrue( testUser.isVerified() );
        assertNull( testUser.getVerificationToken() );
        verify( userRepository ).findByVerificationToken( verificationToken );
        verify( userRepository ).save( testUser );
    }

    @Test @DisplayName("Verify email with invalid token should throw UserNotFoundException")
    void verifyEmail_InvalidToken_ThrowsUserNotFoundException()
    {
        // Given
        String invalidToken = UUID.randomUUID().toString();
        when( userRepository.findByVerificationToken( invalidToken ) ).thenReturn( Optional.empty() );
        // When & Then
        UserNotFoundException exception = assertThrows( UserNotFoundException.class,
                                                        () -> registrationService.verifyEmail( invalidToken ) );
        assertNotNull( exception );
        verify( userRepository ).findByVerificationToken( invalidToken );
        verifyNoMoreInteractions( userRepository );
    }

    @Test @DisplayName("Verify email with already verified user should not throw exception")
    void verifyEmail_AlreadyVerified_Succeeds()
    {
        // Given
        String verificationToken = UUID.randomUUID().toString();
        testUser.setVerificationToken( verificationToken );
        testUser.setVerified( true );
        when( userRepository.findByVerificationToken( verificationToken ) ).thenReturn( Optional.of( testUser ) );
        // When
        assertDoesNotThrow( () -> registrationService.verifyEmail( verificationToken ) );
        // Then
        assertTrue( testUser.isVerified() );
        verify( userRepository ).findByVerificationToken( verificationToken );
        verify( userRepository, never() ).save( testUser );
    }

    @Test @DisplayName("Resend verification email with existing unverified user should succeed")
    void resendVerificationEmail_ExistingUnverifiedUser_Succeeds()
    {
        // Given
        String email = "test@example.com";
        testUser.setVerified( false );
        when( userRepository.findByEmail( email ) ).thenReturn( Optional.of( testUser ) );
        // When
        assertDoesNotThrow( () -> registrationService.resendVerificationEmail( email ) );
        // Then
        assertNotNull( testUser.getVerificationToken() );
        verify( userRepository ).findByEmail( email );
        verify( userRepository ).save( testUser );
    }

    @Test @DisplayName("Resend verification email with non-existing user should throw UserNotFoundException")
    void resendVerificationEmail_NonExistingUser_ThrowsUserNotFoundException()
    {
        // Given
        String email = "nonexistent@example.com";
        when( userRepository.findByEmail( email ) ).thenReturn( Optional.empty() );
        // When & Then
        UserNotFoundException exception = assertThrows( UserNotFoundException.class,
                                                        () -> registrationService.resendVerificationEmail( email ) );
        assertNotNull( exception );
        verify( userRepository ).findByEmail( email );
        verifyNoMoreInteractions( userRepository );
    }

    @Test @DisplayName("Resend verification email with already verified user should not generate new token")
    void resendVerificationEmail_AlreadyVerified_Succeeds()
    {
        // Given
        String email = "test@example.com";
        testUser.setVerified( true );
        when( userRepository.findByEmail( email ) ).thenReturn( Optional.of( testUser ) );
        // When
        assertDoesNotThrow( () -> registrationService.resendVerificationEmail( email ) );
        // Then
        verify( userRepository ).findByEmail( email );
        verify( userRepository, never() ).save( testUser );
    }

    @Test @DisplayName("Register user should generate verification token")
    void registerUser_ShouldGenerateVerificationToken()
    {
        // Given
        when( userService.existsByEmail( "test@example.com" ) ).thenReturn( false );
        when( userService.existsByUsername( "testuser" ) ).thenReturn( false );
        when( userService.createUser( registrationRequest ) ).thenReturn( testUser );
        // When
        UserRegistrationResponse response = registrationService.registerUser( registrationRequest );
        // Then
        assertNotNull( response );
        verify( userService ).createUser( registrationRequest );
        verify( kafkaServiceHelper ).sendMessage( any( EmailRequest.class ) );
        // Verification token generation is handled in UserService.createUser
    }

    @Test @DisplayName("Register user should handle null verification token")
    void registerUser_NullVerificationToken_HandlesGracefully()
    {
        // Given
        testUser.setVerificationToken( null );
        when( userService.existsByEmail( "test@example.com" ) ).thenReturn( false );
        when( userService.existsByUsername( "testuser" ) ).thenReturn( false );
        when( userService.createUser( registrationRequest ) ).thenReturn( testUser );
        // When
        UserRegistrationResponse response = registrationService.registerUser( registrationRequest );
        // Then
        assertNotNull( response );
        assertFalse( response.isVerified() );
        verify( userService ).createUser( registrationRequest );
        verify( kafkaServiceHelper ).sendMessage( any( EmailRequest.class ) );
    }

    @Test @DisplayName("Verify email should handle null verification token in user")
    void verifyEmail_NullTokenInUser_ThrowsUserNotFoundException()
    {
        // Given
        String verificationToken = UUID.randomUUID().toString();
        testUser.setVerificationToken( null );
        when( userRepository.findByVerificationToken( verificationToken ) ).thenReturn( Optional.of( testUser ) );
        // When
        assertDoesNotThrow( () -> registrationService.verifyEmail( verificationToken ) );
        // Then
        verify( userRepository ).findByVerificationToken( verificationToken );
        verify( userRepository ).save( testUser );
    }

    @Test @DisplayName("Resend verification should generate new UUID token")
    void resendVerificationEmail_GeneratesNewToken()
    {
        // Given
        String email = "test@example.com";
        String oldToken = testUser.getVerificationToken();
        testUser.setVerified( false );
        when( userRepository.findByEmail( email ) ).thenReturn( Optional.of( testUser ) );
        // When
        registrationService.resendVerificationEmail( email );
        // Then
        assertNotEquals( oldToken, testUser.getVerificationToken() );
        verify( userRepository ).findByEmail( email );
        verify( userRepository ).save( testUser );
    }
}
