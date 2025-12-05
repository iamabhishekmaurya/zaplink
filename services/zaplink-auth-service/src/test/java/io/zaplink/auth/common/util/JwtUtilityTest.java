package io.zaplink.auth.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.zaplink.auth.common.config.JwtConfig;
import io.zaplink.auth.common.constants.SecurityConstants;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.entity.User;

/**
 * Comprehensive test suite for JwtUtility.
 * Tests all JWT token generation and response building methods.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtility Tests")
class JwtUtilityTest {

    @Mock
    private JwtConfig jwtConfig;
    
    @InjectMocks
    private JwtUtility jwtUtility;
    
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .verified(true)
                .build();
    }

    @Test @DisplayName("generateJwtClaims should create correct claims map")
    void generateJwtClaims_ShouldCreateCorrectClaimsMap() {
        // When
        Map<String, Object> claims = jwtUtility.generateJwtClaims(user);
        
        // Then
        assertNotNull(claims);
        assertEquals(2, claims.size());
        assertEquals(user.getId(), claims.get(SecurityConstants.JWT_CLAIM_USER_ID));
        assertEquals(user.getUsername(), claims.get(SecurityConstants.JWT_CLAIM_USERNAME));
    }

    @Test @DisplayName("generateAccessToken should call jwtConfig with correct parameters")
    void generateAccessToken_ShouldCallJwtConfigWithCorrectParameters() {
        // Given
        String expectedToken = "generated.jwt.token";
        when(jwtConfig.generateToken(user.getEmail(), Map.of(SecurityConstants.JWT_CLAIM_USER_ID, user.getId(), SecurityConstants.JWT_CLAIM_USERNAME, user.getUsername())))
            .thenReturn(expectedToken);
        
        // When
        String actualToken = jwtUtility.generateAccessToken(user);
        
        // Then
        assertEquals(expectedToken, actualToken);
    }

    @Test @DisplayName("generateAccessToken with extra claims should call jwtConfig correctly")
    void generateAccessToken_WithExtraClaims_ShouldCallJwtConfigCorrectly() {
        // Given
        String expectedToken = "custom.jwt.token";
        Map<String, Object> extraClaims = Map.of("custom", "value");
        when(jwtConfig.generateToken(user.getEmail(), extraClaims))
            .thenReturn(expectedToken);
        
        // When
        String actualToken = jwtUtility.generateAccessToken(user, extraClaims);
        
        // Then
        assertEquals(expectedToken, actualToken);
    }

    @Test @DisplayName("buildUserInfo should create correct user info")
    void buildUserInfo_ShouldCreateCorrectUserInfo() {
        // When
        LoginResponse.UserInfo userInfo = jwtUtility.buildUserInfo(user);
        
        // Then
        assertNotNull(userInfo);
        assertEquals(user.getId(), userInfo.getId());
        assertEquals(user.getUsername(), userInfo.getUsername());
        assertEquals(user.getEmail(), userInfo.getEmail());
        assertEquals(user.getFirstName(), userInfo.getFirstName());
        assertEquals(user.getLastName(), userInfo.getLastName());
        assertEquals(user.isVerified(), userInfo.isVerified());
    }

    @Test @DisplayName("buildLoginResponse should create complete response")
    void buildLoginResponse_ShouldCreateCompleteResponse() {
        // Given
        String accessToken = "access.token";
        String refreshToken = "refresh.token";
        long expectedExpiration = 1800L;
        when(jwtConfig.getJwtExpiration()).thenReturn(expectedExpiration);
        
        // When
        LoginResponse response = jwtUtility.buildLoginResponse(user, accessToken, refreshToken);
        
        // Then
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(expectedExpiration, response.getExpiresIn());
        assertNotNull(response.getUserInfo());
        
        // Verify user info
        LoginResponse.UserInfo userInfo = response.getUserInfo();
        assertEquals(user.getId(), userInfo.getId());
        assertEquals(user.getUsername(), userInfo.getUsername());
        assertEquals(user.getEmail(), userInfo.getEmail());
        assertEquals(user.getFirstName(), userInfo.getFirstName());
        assertEquals(user.getLastName(), userInfo.getLastName());
        assertEquals(user.isVerified(), userInfo.isVerified());
    }

    @Test @DisplayName("buildLoginResponse should work with different token types")
    void buildLoginResponse_ShouldWorkWithDifferentTokenTypes() {
        // Given
        String accessToken = "custom.access";
        String refreshToken = "custom.refresh";
        long customExpiration = 7200L;
        when(jwtConfig.getJwtExpiration()).thenReturn(customExpiration);
        
        // When
        LoginResponse response = jwtUtility.buildLoginResponse(user, accessToken, refreshToken);
        
        // Then
        assertEquals(customExpiration, response.getExpiresIn());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }
}
