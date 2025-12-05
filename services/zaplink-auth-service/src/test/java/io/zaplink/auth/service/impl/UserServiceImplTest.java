package io.zaplink.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.zaplink.auth.common.constants.SecurityConstants;
import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.entity.Role;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.RoleRepository;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.service.helper.UserHelper;

/**
 * Comprehensive test suite for UserServiceImpl.
 * Tests all user management operations including creation, updates, and validation.
 */
@ExtendWith(MockitoExtension.class) @DisplayName("UserService Implementation Tests")
class UserServiceImplTest
{
    @Mock
    private UserRepository          userRepository;
    @Mock
    private RoleRepository          roleRepository;
    @Mock
    private PasswordEncoder         passwordEncoder;
    @Mock
    private UserHelper              userHelper;
    @InjectMocks
    private UserServiceImpl         userService;
    private User                    testUser;
    private UserRegistrationRequest registrationRequest;
    private Role                    userRole;
    @BeforeEach
    void setUp()
    {
        userRole = Role.builder().id( 1L ).name( SecurityConstants.ROLE_USER ).description( "Standard user role" )
                .build();
        testUser = User.builder().id( 1L ).email( "test@example.com" ).username( "testuser" )
                .password( "encodedPassword" ).firstName( "Test" ).lastName( "User" ).phoneNumber( "1234567890" )
                .active( true ).verified( true ).roles( Set.of( userRole ) )
                .verificationToken( UUID.randomUUID().toString() ).createdAt( Instant.now() ).build();
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setEmail( "test@example.com" );
        registrationRequest.setUsername( "testuser" );
        registrationRequest.setPassword( "password123" );
        registrationRequest.setFirstName( "Test" );
        registrationRequest.setLastName( "User" );
        registrationRequest.setPhoneNumber( "1234567890" );
    }

    @Test @DisplayName("Find user by email should return user when found")
    void findByEmail_UserFound_ReturnsUser()
    {
        // Given
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( Optional.of( testUser ) );
        // When
        Optional<User> result = userService.findByEmail( "test@example.com" );
        // Then
        assertTrue( result.isPresent() );
        assertEquals( testUser, result.get() );
        verify( userRepository ).findByEmail( "test@example.com" );
    }

    @Test @DisplayName("Find user by email should return empty when not found")
    void findByEmail_UserNotFound_ReturnsEmpty()
    {
        // Given
        when( userRepository.findByEmail( "nonexistent@example.com" ) ).thenReturn( Optional.empty() );
        // When
        Optional<User> result = userService.findByEmail( "nonexistent@example.com" );
        // Then
        assertFalse( result.isPresent() );
        verify( userRepository ).findByEmail( "nonexistent@example.com" );
    }

    @Test @DisplayName("Find user by username should return user when found")
    void findByUsername_UserFound_ReturnsUser()
    {
        // Given
        when( userRepository.findByUsername( "testuser" ) ).thenReturn( Optional.of( testUser ) );
        // When
        Optional<User> result = userService.findByUsername( "testuser" );
        // Then
        assertTrue( result.isPresent() );
        assertEquals( testUser, result.get() );
        verify( userRepository ).findByUsername( "testuser" );
    }

    @Test @DisplayName("Find user by username should return empty when not found")
    void findByUsername_UserNotFound_ReturnsEmpty()
    {
        // Given
        when( userRepository.findByUsername( "nonexistent" ) ).thenReturn( Optional.empty() );
        // When
        Optional<User> result = userService.findByUsername( "nonexistent" );
        // Then
        assertFalse( result.isPresent() );
        verify( userRepository ).findByUsername( "nonexistent" );
    }

    @Test @DisplayName("Create user with valid data should succeed")
    void createUser_ValidData_Succeeds()
    {
        // Given
        when( passwordEncoder.encode( "password123" ) ).thenReturn( "encodedPassword" );
        when( roleRepository.findByName( SecurityConstants.ROLE_USER ) ).thenReturn( Optional.of( userRole ) );
        when( userRepository.save( any( User.class ) ) ).thenReturn( testUser );
        // When
        User result = userService.createUser( registrationRequest );
        // Then
        assertNotNull( result );
        assertEquals( testUser, result );
        verify( passwordEncoder ).encode( "password123" );
        verify( roleRepository ).findByName( SecurityConstants.ROLE_USER );
        verify( userRepository ).save( any( User.class ) );
    }

    @Test @DisplayName("Create user should create default USER role if not exists")
    void createUser_DefaultRoleNotExists_CreatesRole()
    {
        // Given
        when( passwordEncoder.encode( "password123" ) ).thenReturn( "encodedPassword" );
        when( roleRepository.findByName( SecurityConstants.ROLE_USER ) ).thenReturn( Optional.empty() );
        when( roleRepository.save( any( Role.class ) ) ).thenReturn( userRole );
        when( userRepository.save( any( User.class ) ) ).thenReturn( testUser );
        // When
        User result = userService.createUser( registrationRequest );
        // Then
        assertNotNull( result );
        verify( passwordEncoder ).encode( "password123" );
        verify( roleRepository ).findByName( SecurityConstants.ROLE_USER );
        verify( roleRepository ).save( any( Role.class ) );
        verify( userRepository ).save( any( User.class ) );
    }

    @Test @DisplayName("Save user should return saved user")
    void saveUser_ValidUser_ReturnsSavedUser()
    {
        // Given
        when( userRepository.save( testUser ) ).thenReturn( testUser );
        // When
        User result = userService.saveUser( testUser );
        // Then
        assertEquals( testUser, result );
        verify( userRepository ).save( testUser );
    }

    @Test @DisplayName("Check if user exists by email should return true when exists")
    void existsByEmail_UserExists_ReturnsTrue()
    {
        // Given
        when( userRepository.existsByEmail( "test@example.com" ) ).thenReturn( true );
        // When
        boolean result = userService.existsByEmail( "test@example.com" );
        // Then
        assertTrue( result );
        verify( userRepository ).existsByEmail( "test@example.com" );
    }

    @Test @DisplayName("Check if user exists by email should return false when not exists")
    void existsByEmail_UserNotExists_ReturnsFalse()
    {
        // Given
        when( userRepository.existsByEmail( "nonexistent@example.com" ) ).thenReturn( false );
        // When
        boolean result = userService.existsByEmail( "nonexistent@example.com" );
        // Then
        assertFalse( result );
        verify( userRepository ).existsByEmail( "nonexistent@example.com" );
    }

    @Test @DisplayName("Check if user exists by username should return true when exists")
    void existsByUsername_UserExists_ReturnsTrue()
    {
        // Given
        when( userRepository.existsByUsername( "testuser" ) ).thenReturn( true );
        // When
        boolean result = userService.existsByUsername( "testuser" );
        // Then
        assertTrue( result );
        verify( userRepository ).existsByUsername( "testuser" );
    }

    @Test @DisplayName("Check if user exists by username should return false when not exists")
    void existsByUsername_UserNotExists_ReturnsFalse()
    {
        // Given
        when( userRepository.existsByUsername( "nonexistent" ) ).thenReturn( false );
        // When
        boolean result = userService.existsByUsername( "nonexistent" );
        // Then
        assertFalse( result );
        verify( userRepository ).existsByUsername( "nonexistent" );
    }

    @Test @DisplayName("Update user should succeed")
    void updateUser_ValidUser_Succeeds()
    {
        // Given
        User updatedUser = User.builder().firstName( "Updated" ).lastName( "Name" ).phoneNumber( "9876543210" ).build();
        when( userHelper.findUserByIdOrThrow( 1L, "update" ) ).thenReturn( testUser );
        when( userHelper.updateUser( eq( testUser ), any() ) ).thenReturn( testUser );
        // When
        assertDoesNotThrow( () -> userService.updateUser( 1L, updatedUser ) );
        // Then
        verify( userHelper ).findUserByIdOrThrow( 1L, "update" );
        verify( userHelper ).updateUser( eq( testUser ), any() );
    }

    @Test @DisplayName("Deactivate user should succeed")
    void deactivateUser_ValidUser_Succeeds()
    {
        // Given
        when( userHelper.findUserByIdOrThrow( 1L, "deactivation" ) ).thenReturn( testUser );
        when( userHelper.updateUser( eq( testUser ), any() ) ).thenAnswer( invocation -> {
            Consumer<User> updater = invocation.getArgument( 1 );
            updater.accept( testUser );
            return testUser;
        } );
        // When
        assertDoesNotThrow( () -> userService.deactivateUser( 1L ) );
        // Then
        verify( userHelper ).findUserByIdOrThrow( 1L, "deactivation" );
        verify( userHelper ).updateUser( eq( testUser ), any() );
        assertFalse( testUser.isActive() );
    }

    @Test @DisplayName("Activate user should succeed")
    void activateUser_ValidUser_Succeeds()
    {
        // Given
        testUser.setActive( false );
        when( userHelper.findUserByIdOrThrow( 1L, "activation" ) ).thenReturn( testUser );
        when( userHelper.updateUser( eq( testUser ), any() ) ).thenAnswer( invocation -> {
            Consumer<User> updater = invocation.getArgument( 1 );
            updater.accept( testUser );
            return testUser;
        } );
        // When
        assertDoesNotThrow( () -> userService.activateUser( 1L ) );
        // Then
        verify( userHelper ).findUserByIdOrThrow( 1L, "activation" );
        verify( userHelper ).updateUser( eq( testUser ), any() );
        assertTrue( testUser.isActive() );
    }

    @Test @DisplayName("Create user should set proper initial values")
    void createUser_ShouldSetInitialValues()
    {
        // Given
        when( passwordEncoder.encode( "password123" ) ).thenReturn( "encodedPassword" );
        when( roleRepository.findByName( SecurityConstants.ROLE_USER ) ).thenReturn( Optional.of( userRole ) );
        when( userRepository.save( any( User.class ) ) ).thenAnswer( invocation -> invocation.getArgument( 0 ) );
        // When
        User result = userService.createUser( registrationRequest );
        // Then
        assertNotNull( result );
        assertEquals( registrationRequest.getUsername(), result.getUsername() );
        assertEquals( registrationRequest.getEmail(), result.getEmail() );
        assertEquals( "encodedPassword", result.getPassword() );
        assertEquals( registrationRequest.getFirstName(), result.getFirstName() );
        assertEquals( registrationRequest.getLastName(), result.getLastName() );
        assertEquals( registrationRequest.getPhoneNumber(), result.getPhoneNumber() );
        assertTrue( result.isActive() );
        assertFalse( result.isVerified() );
        assertNotNull( result.getVerificationToken() );
        assertNotNull( result.getCreatedAt() );
        assertEquals( 1, result.getRoles().size() );
        assertTrue( result.getRoles().contains( userRole ) );
        verify( passwordEncoder ).encode( "password123" );
        verify( roleRepository ).findByName( SecurityConstants.ROLE_USER );
        verify( userRepository ).save( any( User.class ) );
    }

    @Test @DisplayName("Update user should only update modifiable fields")
    void updateUser_ShouldUpdateOnlyModifiableFields()
    {
        // Given
        User updatedUser = User.builder().firstName( "Updated" ).lastName( "Name" ).phoneNumber( "9876543210" )
                .email( "shouldnotchange@example.com" ) // Should not be updated
                .username( "shouldnotchange" ) // Should not be updated
                .build();
        when( userHelper.findUserByIdOrThrow( 1L, "update" ) ).thenReturn( testUser );
        when( userHelper.updateUser( eq( testUser ), any() ) ).thenAnswer( invocation -> {
            Consumer<User> updater = invocation.getArgument( 1 );
            updater.accept( testUser );
            return testUser;
        } );
        // When
        userService.updateUser( 1L, updatedUser );
        // Then
        assertEquals( "Updated", testUser.getFirstName() );
        assertEquals( "Name", testUser.getLastName() );
        assertEquals( "9876543210", testUser.getPhoneNumber() );
        // These should not have changed
        assertEquals( "test@example.com", testUser.getEmail() );
        assertEquals( "testuser", testUser.getUsername() );
        verify( userHelper ).findUserByIdOrThrow( 1L, "update" );
        verify( userHelper ).updateUser( eq( testUser ), any() );
    }

    @Test @DisplayName("Create user should handle role assignment properly")
    void createUser_ShouldAssignProperRoles()
    {
        // Given
        when( passwordEncoder.encode( "password123" ) ).thenReturn( "encodedPassword" );
        when( roleRepository.findByName( SecurityConstants.ROLE_USER ) ).thenReturn( Optional.of( userRole ) );
        when( userRepository.save( any( User.class ) ) ).thenAnswer( invocation -> invocation.getArgument( 0 ) );
        // When
        User result = userService.createUser( registrationRequest );
        // Then
        assertNotNull( result.getRoles() );
        assertEquals( 1, result.getRoles().size() );
        assertTrue( result.getRoles().contains( userRole ) );
        verify( roleRepository ).findByName( SecurityConstants.ROLE_USER );
    }
}
