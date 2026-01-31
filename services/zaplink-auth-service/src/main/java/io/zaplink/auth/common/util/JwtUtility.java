package io.zaplink.auth.common.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.zaplink.auth.common.constants.SecurityConstants;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.entity.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Simplified JWT utility class for token generation only.
 * JWT validation has been moved to API Gateway.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j @Component
public class JwtUtility
{
    @Value("${jwt.secret:mySecretKey}")
    private String secret;
    @Value("${jwt.expiration:1800}")
    private Long   jwtExpiration;
    /**
     * Gets the signing key for JWT tokens.
     */
    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor( secret.getBytes() );
    }

    /**
     * Generates JWT claims for a user with organization and role information.
     * 
     * @param user The user to generate claims for
     * @return Map of JWT claims
     */
    public Map<String, Object> generateJwtClaims( User user )
    {
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put( SecurityConstants.CLAIM_USER_ID, user.getId() );
        claims.put( SecurityConstants.CLAIM_USERNAME, user.getUsername() );
        
        // Add organization and role information if available
        // This will be populated during login from team membership
        claims.put( SecurityConstants.CLAIM_ORG_ID, 1L ); // Default org for now
        claims.put( SecurityConstants.CLAIM_ROLE, SecurityConstants.ROLE_USER ); // Default role
        
        return claims;
    }

    /**
     * Generates JWT claims for a user with custom organization and role.
     * 
     * @param user The user to generate claims for
     * @param orgId The organization ID
     * @param role The user role
     * @param teamId The team ID (optional)
     * @return Map of JWT claims
     */
    public Map<String, Object> generateJwtClaims( User user, Long orgId, String role, Long teamId )
    {
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put( SecurityConstants.CLAIM_USER_ID, user.getId() );
        claims.put( SecurityConstants.CLAIM_USERNAME, user.getUsername() );
        claims.put( SecurityConstants.CLAIM_ORG_ID, orgId );
        claims.put( SecurityConstants.CLAIM_ROLE, role );
        
        if ( teamId != null )
        {
            claims.put( SecurityConstants.CLAIM_TEAM_ID, teamId );
        }
        
        return claims;
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
        return generateToken( user.getEmail(), extraClaims );
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
        return generateToken( user.getEmail(), extraClaims );
    }

    /**
     * Generates a JWT token for the given username with additional claims.
     * 
     * @param username The username to include in the token
     * @param extraClaims Additional claims to include in the token
     * @return The generated JWT token string
     */
    private String generateToken( String username, Map<String, Object> extraClaims )
    {
        Instant now = Instant.now();
        Instant expiryDate = now.plus( jwtExpiration, ChronoUnit.SECONDS );
        return Jwts.builder().setClaims( extraClaims ).setSubject( username ).setIssuedAt( Date.from( now ) )
                .setExpiration( Date.from( expiryDate ) ).signWith( getSigningKey() ).compact();
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
        return new LoginResponse( accessToken, refreshToken, jwtExpiration, userInfo );
    }

    /**
     * Gets the configured JWT expiration time in seconds.
     * 
     * @return JWT expiration time in seconds
     */
    public Long getJwtExpiration()
    {
        return jwtExpiration;
    }
}
