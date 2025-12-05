package io.zaplink.auth.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.zaplink.auth.common.constants.SecurityConstants;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;

/**
 * Comprehensive test suite for CustomUserDetailsService.
 * Tests user loading and authentication details generation.
 */
@ExtendWith(MockitoExtension.class) @DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest
{
    @Mock
    private UserRepository           userRepository;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
    private User                     testUser;
    @BeforeEach
    void setUp()
    {
        testUser = User.builder().id( 1L ).email( "test@example.com" ).password( "encodedPassword" ).firstName( "Test" )
                .lastName( "User" ).active( true ).verified( true ).build();
    }

    @Test @DisplayName("Load user by username with valid email should return UserDetails")
    void loadUserByUsername_ValidEmail_ReturnsUserDetails()
    {
        // Given
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( java.util.Optional.of( testUser ) );
        try (var mockedStringUtil = mockStatic( io.zaplink.auth.common.util.StringUtil.class ))
        {
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.createRoleName( SecurityConstants.ROLE_USER ) )
                    .thenReturn( "ROLE_USER" );
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.appendValue( anyString(), anyString() ) )
                    .thenReturn( "User not found with email: test@example.com" );
            // When
            UserDetails result = userDetailsService.loadUserByUsername( "test@example.com" );
            // Then
            assertNotNull( result );
            assertEquals( "test@example.com", result.getUsername() );
            assertEquals( "encodedPassword", result.getPassword() );
            assertTrue( result.isEnabled() );
            assertTrue( result.isAccountNonExpired() );
            assertTrue( result.isAccountNonLocked() );
            assertTrue( result.isCredentialsNonExpired() );
            // Check authorities
            assertEquals( 1, result.getAuthorities().size() );
            assertTrue( result.getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_USER" ) ) );
            verify( userRepository ).findByEmail( "test@example.com" );
            mockedStringUtil.verify( () -> io.zaplink.auth.common.util.StringUtil
                    .createRoleName( SecurityConstants.ROLE_USER ) );
        }
    }

    @Test @DisplayName("Load user by username with non-existent email should throw UsernameNotFoundException")
    void loadUserByUsername_NonExistentEmail_ThrowsUsernameNotFoundException()
    {
        // Given
        when( userRepository.findByEmail( "nonexistent@example.com" ) ).thenReturn( java.util.Optional.empty() );
        // When & Then
        UsernameNotFoundException exception = assertThrows( UsernameNotFoundException.class, () -> userDetailsService
                .loadUserByUsername( "nonexistent@example.com" ) );
        assertNotNull( exception );
        assertTrue( exception.getMessage().contains( "nonexistent@example.com" ) );
        verify( userRepository ).findByEmail( "nonexistent@example.com" );
    }

    @Test @DisplayName("Load user by username with inactive user should throw UsernameNotFoundException")
    void loadUserByUsername_InactiveUser_ThrowsUsernameNotFoundException()
    {
        // Given
        testUser.setActive( false );
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( java.util.Optional.of( testUser ) );
        // When & Then
        UsernameNotFoundException exception = assertThrows( UsernameNotFoundException.class, () -> userDetailsService
                .loadUserByUsername( "test@example.com" ) );
        assertNotNull( exception );
        assertTrue( exception.getMessage().contains( "test@example.com" ) );
        verify( userRepository ).findByEmail( "test@example.com" );
    }

    @Test @DisplayName("Load user by username should preserve user account status")
    void loadUserByUsername_ShouldPreserveAccountStatus()
    {
        // Given
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( java.util.Optional.of( testUser ) );
        try (var mockedStringUtil = mockStatic( io.zaplink.auth.common.util.StringUtil.class ))
        {
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.createRoleName( SecurityConstants.ROLE_USER ) )
                    .thenReturn( "ROLE_USER" );
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.appendValue( anyString(), anyString() ) )
                    .thenReturn( "User not found with email: test@example.com" );
            // When
            UserDetails result = userDetailsService.loadUserByUsername( "test@example.com" );
            // Then
            assertTrue( result.isEnabled() );
            assertTrue( result.isAccountNonExpired() );
            assertTrue( result.isAccountNonLocked() );
            assertTrue( result.isCredentialsNonExpired() );
            verify( userRepository ).findByEmail( "test@example.com" );
        }
    }

    @Test @DisplayName("Load user by username with null email should handle gracefully")
    void loadUserByUsername_NullEmail_HandlesGracefully()
    {
        // Given
        when( userRepository.findByEmail( null ) ).thenReturn( java.util.Optional.empty() );
        // When & Then
        UsernameNotFoundException exception = assertThrows( UsernameNotFoundException.class,
                                                            () -> userDetailsService.loadUserByUsername( null ) );
        assertNotNull( exception );
        verify( userRepository ).findByEmail( null );
    }

    @Test @DisplayName("Load user by username should use Collections.singletonList for authorities")
    void loadUserByUsername_ShouldUseCollectionsSingletonList()
    {
        // Given
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( java.util.Optional.of( testUser ) );
        try (var mockedStringUtil = mockStatic( io.zaplink.auth.common.util.StringUtil.class ))
        {
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.createRoleName( SecurityConstants.ROLE_USER ) )
                    .thenReturn( "ROLE_USER" );
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.appendValue( anyString(), anyString() ) )
                    .thenReturn( "User not found with email: test@example.com" );
            // When
            UserDetails result = userDetailsService.loadUserByUsername( "test@example.com" );
            // Then
            assertNotNull( result.getAuthorities() );
            assertEquals( 1, result.getAuthorities().size() );
            verify( userRepository ).findByEmail( "test@example.com" );
            mockedStringUtil.verify( () -> io.zaplink.auth.common.util.StringUtil
                    .createRoleName( SecurityConstants.ROLE_USER ) );
        }
    }

    @Test @DisplayName("Load user by username should build User object with correct parameters")
    void loadUserByUsername_ShouldBuildUserWithCorrectParameters()
    {
        // Given
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( java.util.Optional.of( testUser ) );
        try (var mockedStringUtil = mockStatic( io.zaplink.auth.common.util.StringUtil.class ))
        {
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.createRoleName( SecurityConstants.ROLE_USER ) )
                    .thenReturn( "ROLE_USER" );
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.appendValue( anyString(), anyString() ) )
                    .thenReturn( "User not found with email: test@example.com" );
            // When
            UserDetails result = userDetailsService.loadUserByUsername( "test@example.com" );
            // Then
            assertNotNull( result );
            assertEquals( "test@example.com", result.getUsername() );
            assertEquals( "encodedPassword", result.getPassword() );
            assertTrue( result.isEnabled() );
            assertTrue( result.isAccountNonExpired() );
            assertTrue( result.isAccountNonLocked() );
            assertTrue( result.isCredentialsNonExpired() );
            verify( userRepository ).findByEmail( "test@example.com" );
            mockedStringUtil.verify( () -> io.zaplink.auth.common.util.StringUtil
                    .createRoleName( SecurityConstants.ROLE_USER ) );
        }
    }

    @Test @DisplayName("Load user by username with verified false should still work")
    void loadUserByUsername_UnverifiedUser_ShouldStillWork()
    {
        // Given
        testUser.setVerified( false );
        when( userRepository.findByEmail( "test@example.com" ) ).thenReturn( java.util.Optional.of( testUser ) );
        try (var mockedStringUtil = mockStatic( io.zaplink.auth.common.util.StringUtil.class ))
        {
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.createRoleName( SecurityConstants.ROLE_USER ) )
                    .thenReturn( "ROLE_USER" );
            mockedStringUtil
                    .when( () -> io.zaplink.auth.common.util.StringUtil.appendValue( anyString(), anyString() ) )
                    .thenReturn( "User not found with email: test@example.com" );
            // When
            UserDetails result = userDetailsService.loadUserByUsername( "test@example.com" );
            // Then
            assertNotNull( result );
            assertEquals( "test@example.com", result.getUsername() );
            assertTrue( result.isEnabled() ); // Active status, not verified status
            verify( userRepository ).findByEmail( "test@example.com" );
            mockedStringUtil.verify( () -> io.zaplink.auth.common.util.StringUtil
                    .createRoleName( SecurityConstants.ROLE_USER ) );
        }
    }
}
