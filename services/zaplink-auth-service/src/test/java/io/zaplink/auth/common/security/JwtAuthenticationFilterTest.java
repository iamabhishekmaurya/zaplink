package io.zaplink.auth.common.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import io.zaplink.auth.common.config.JwtConfig;
import io.zaplink.auth.common.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Comprehensive test suite for JwtAuthenticationFilter.
 * Tests JWT token validation and authentication setup.
 */
@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Mock
    private JwtConfig jwtConfig;
    
    @Mock
    private UserDetailsService userDetailsService;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtConfig, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test @DisplayName("Filter should pass through when no auth header")
    void doFilterInternal_ShouldPassThroughWhenNoAuthHeader() throws ServletException, IOException {
        // Given
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn(null);
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test @DisplayName("Filter should pass through when auth header doesn't start with Bearer")
    void doFilterInternal_ShouldPassThroughWhenAuthHeaderNotBearer() throws ServletException, IOException {
        // Given
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Basic dGVzdDp0ZXN0");
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test @DisplayName("Filter should authenticate when valid JWT token")
    void doFilterInternal_ShouldAuthenticateWhenValidJwtToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String userEmail = "test@example.com";
        UserDetails userDetails = User.builder()
                .username(userEmail)
                .password("password")
                .authorities("ROLE_USER")
                .build();
        
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Bearer " + token);
        when(jwtConfig.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtConfig.validateToken(token, userEmail)).thenReturn(true);
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        UsernamePasswordAuthenticationToken auth = 
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userDetails, auth.getPrincipal());
        assertNotNull(auth.getDetails());
    }

    @Test @DisplayName("Filter should not authenticate when token extraction returns null")
    void doFilterInternal_ShouldNotAuthenticateWhenTokenExtractionReturnsNull() throws ServletException, IOException {
        // Given
        String token = "invalid.token";
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Bearer " + token);
        when(jwtConfig.extractUsername(token)).thenReturn(null);
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test @DisplayName("Filter should not authenticate when token validation fails")
    void doFilterInternal_ShouldNotAuthenticateWhenTokenValidationFails() throws ServletException, IOException {
        // Given
        String token = "invalid.jwt.token";
        String userEmail = "test@example.com";
        UserDetails userDetails = User.builder()
                .username(userEmail)
                .password("password")
                .authorities("ROLE_USER")
                .build();
        
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Bearer " + token);
        when(jwtConfig.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtConfig.validateToken(token, userEmail)).thenReturn(false);
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test @DisplayName("Filter should not authenticate when authentication already exists")
    void doFilterInternal_ShouldNotAuthenticateWhenAuthenticationAlreadyExists() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String userEmail = "test@example.com";
        
        // Set existing authentication
        UsernamePasswordAuthenticationToken existingAuth = 
            new UsernamePasswordAuthenticationToken("existingUser", null, null);
        SecurityContextHolder.getContext().setAuthentication(existingAuth);
        
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Bearer " + token);
        when(jwtConfig.extractUsername(token)).thenReturn(userEmail);
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertEquals(existingAuth, SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtConfig, never()).validateToken(any(), any());
    }

    @Test @DisplayName("Filter should handle empty auth header")
    void doFilterInternal_ShouldHandleEmptyAuthHeader() throws ServletException, IOException {
        // Given
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("");
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test @DisplayName("Filter should handle Bearer prefix only")
    void doFilterInternal_ShouldHandleBearerPrefixOnly() throws ServletException, IOException {
        // Given
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Bearer ");
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test @DisplayName("Filter should handle user service exception gracefully")
    void doFilterInternal_ShouldHandleUserServiceExceptionGracefully() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String userEmail = "nonexistent@example.com";
        
        when(request.getHeader(SecurityConstants.AUTH_HEADER)).thenReturn("Bearer " + token);
        when(jwtConfig.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail))
            .thenThrow(new RuntimeException("User not found"));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        });
        
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
