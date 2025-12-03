package io.zaplink.auth.common.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import io.zaplink.auth.common.config.JwtConfig;
import io.zaplink.auth.common.constants.SecurityConstants;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.entity.User;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for JWT token operations.
 * Provides reusable methods for JWT token generation and response building.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Component 
@RequiredArgsConstructor
public class JwtUtility
{
    private final JwtConfig jwtConfig;
    /**
     * Generates JWT claims for a user.
     * 
     * @param user The user to generate claims for
     * @return Map of JWT claims
     */
    public Map<String, Object> generateJwtClaims( User user )
    {
        return Map.of( SecurityConstants.JWT_CLAIM_USER_ID, user.getId(), SecurityConstants.JWT_CLAIM_USERNAME,
                       user.getUsername() );
    }

    /**
     * Generates JWT access token for a user.
     * 
     * @param user The user to generate token for
     * @return The generated JWT access token
     */
    public String generateAccessToken( User user )
    {
        Map<String, Object> extraClaims = generateJwtClaims( user );
        return jwtConfig.generateToken( user.getEmail(), extraClaims );
    }

    /**
     * Generates JWT access token with custom claims.
     * 
     * @param user The user to generate token for
     * @param extraClaims Additional claims to include
     * @return The generated JWT access token
     */
    public String generateAccessToken( User user, Map<String, Object> extraClaims )
    {
        return jwtConfig.generateToken( user.getEmail(), extraClaims );
    }

    /**
     * Builds user info for login response.
     * 
     * @param user The user to build info for
     * @return LoginResponse.UserInfo object
     */
    public LoginResponse.UserInfo buildUserInfo( User user )
    {
        return new LoginResponse.UserInfo( user.getId(),
                                           user.getUsername(),
                                           user.getEmail(),
                                           user.getFirstName(),
                                           user.getLastName(),
                                           user.isVerified() );
    }

    /**
     * Builds complete login response.
     * 
     * @param user The authenticated user
     * @param accessToken The JWT access token
     * @param refreshToken The refresh token
     * @return Complete LoginResponse
     */
    public LoginResponse buildLoginResponse( User user, String accessToken, String refreshToken )
    {
        LoginResponse.UserInfo userInfo = buildUserInfo( user );
        return new LoginResponse( accessToken, refreshToken, jwtConfig.getJwtExpiration(), userInfo );
    }
}
