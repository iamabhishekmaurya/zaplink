package io.zaplink.apigateway.common.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.zaplink.apigateway.common.constant.LogConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT (JSON Web Token) configuration and utility class.
 * Provides methods for token generation, validation, and claims extraction.
 * Uses HMAC-SHA256 algorithm for token signing.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j
@Component 
public class JwtConfig
{
    @Value("${jwt.secret:mySecretKey}")
    private String secret;
    @Value("${jwt.expiration:1800}")
    private Long   jwtExpiration;
    /**
     * Retrieves the signing key for JWT tokens.
     * Converts the secret string to a secure HMAC key.
     * 
     * @return SecretKey for JWT signing operations
     */
    private SecretKey getSigningKey()
    {
        log.debug( LogConstants.LOG_GENERATING_HMAC_SIGNING_KEY );
        return Keys.hmacShaKeyFor( secret.getBytes() );
    }

    /**
     * Extracts the username (subject) from a JWT token.
     * 
     * @param token The JWT token to extract username from
     * @return The username contained in the token
     */
    public String extractUsername( String token )
    {
        log.debug( LogConstants.LOG_EXTRACTING_USERNAME_FROM_TOKEN );
        return extractClaim( token, Claims::getSubject );
    }

    /**
     * Extracts the expiration date from a JWT token.
     * 
     * @param token The JWT token to extract expiration from
     * @return The expiration date of the token
     */
    public Date extractExpiration( String token )
    {
        log.debug( LogConstants.LOG_EXTRACTING_EXPIRATION_FROM_TOKEN );
        return extractClaim( token, Claims::getExpiration );
    }

    /**
     * Extracts a specific claim from a JWT token using a claims resolver function.
     * 
     * @param token The JWT token to extract claim from
     * @param claimsResolver Function to extract the desired claim
     * @param <T> Type of the claim to extract
     * @return The extracted claim value
     */
    public <T> T extractClaim( String token, Function<Claims, T> claimsResolver )
    {
        log.debug( LogConstants.LOG_EXTRACTING_CLAIM_FROM_TOKEN );
        final Claims claims = extractAllClaims( token );
        return claimsResolver.apply( claims );
    }

    /**
     * Extracts all claims from a JWT token.
     * Parses the token and validates the signature.
     * 
     * @param token The JWT token to parse
     * @return All claims contained in the token
     */
    private Claims extractAllClaims( String token )
    {
        log.debug( LogConstants.LOG_PARSING_JWT_TOKEN_SIGNATURE );
        return Jwts.parserBuilder().setSigningKey( getSigningKey() ).build().parseClaimsJws( token ).getBody();
    }

    /**
     * Checks if a JWT token has expired.
     * 
     * @param token The JWT token to check
     * @return true if token is expired, false otherwise
     */
    private Boolean isTokenExpired( String token )
    {
        log.debug( LogConstants.LOG_CHECKING_JWT_TOKEN_EXPIRATION );
        return extractExpiration( token ).before( Date.from( Instant.now() ) );
    }

    /**
     * Generates a JWT token for the given username.
     * Uses default claims and configured expiration.
     * 
     * @param username The username to include in the token
     * @return The generated JWT token string
     */
    public String generateToken( String username )
    {
        log.debug( LogConstants.LOG_GENERATING_JWT_TOKEN, username );
        return createToken( Map.of(), username );
    }

    /**
     * Generates a JWT token for the given username with additional claims.
     * Allows custom claims to be included in the token.
     * 
     * @param username The username to include in the token
     * @param extraClaims Additional claims to include in the token
     * @return The generated JWT token string
     */
    public String generateToken( String username, Map<String, Object> extraClaims )
    {
        log.debug( LogConstants.LOG_GENERATING_JWT_TOKEN_WITH_CLAIMS, username, extraClaims.size() );
        return createToken( extraClaims, username );
    }

    /**
     * Creates a JWT token with specified claims and subject.
     * Sets issued date, expiration date, and signs the token.
     * 
     * @param claims The claims to include in the token
     * @param subject The subject (username) of the token
     * @return The generated JWT token string
     */
    private String createToken( Map<String, Object> claims, String subject )
    {
        log.debug( LogConstants.LOG_CREATING_JWT_TOKEN, subject, jwtExpiration );
        Instant now = Instant.now();
        Instant expiryDate = now.plus( jwtExpiration, ChronoUnit.SECONDS );
        String token = Jwts.builder().setClaims( claims ).setSubject( subject ).setIssuedAt( Date.from( now ) )
                .setExpiration( Date.from( expiryDate ) ).signWith( getSigningKey() ).compact();
        log.debug( LogConstants.LOG_JWT_TOKEN_CREATED_SUCCESSFULLY, expiryDate );
        return token;
    }

    /**
     * Validates a JWT token against the expected username.
     * Checks token signature, expiration, and subject match.
     * 
     * @param token The JWT token to validate
     * @param username The expected username in the token
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken( String token, String username )
    {
        try
        {
            log.debug( LogConstants.LOG_VALIDATING_JWT_TOKEN, username );
            final String extractedUsername = extractUsername( token );
            boolean isValid = ( extractedUsername.equals( username ) && !isTokenExpired( token ) );
            if ( isValid )
            {
                log.debug( LogConstants.LOG_JWT_TOKEN_VALIDATION_SUCCESSFUL, username );
            }
            else
            {
                log.warn( LogConstants.LOG_JWT_TOKEN_VALIDATION_FAILED, username, extractedUsername.equals( username ),
                          isTokenExpired( token ) );
            }
            return isValid;
        }
        catch ( Exception e )
        {
            log.error( LogConstants.LOG_JWT_TOKEN_VALIDATION_EXCEPTION, e.getMessage() );
            return false;
        }
    }

    /**
     * Gets the configured JWT expiration time in seconds.
     * 
     * @return JWT expiration time in seconds
     */
    public Long getJwtExpiration()
    {
        log.debug( LogConstants.LOG_JWT_EXPIRATION_TIME, jwtExpiration );
        return jwtExpiration;
    }
}
