package io.zaplink.auth.service.impl;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.exception.InvalidCredentialsException;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.common.util.JwtUtility;
import io.zaplink.auth.common.util.TokenUtility;
import io.zaplink.auth.dto.request.LoginRequest;
import io.zaplink.auth.dto.request.PasswordResetRequest;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.entity.RefreshToken;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.RefreshTokenRepository;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.service.AuthService;
import io.zaplink.auth.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the AuthService interface.
 * Provides authentication, token management, and password reset functionality.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j @Service @Transactional @RequiredArgsConstructor
public class AuthServiceImpl
    implements
    AuthService
{
    private final AuthenticationManager  authenticationManager;
    private final UserRepository         userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder        passwordEncoder;
    private final UserHelper             userHelper;
    private final TokenUtility           tokenUtility;
    private final JwtUtility             jwtUtility;
    /**
     * Authenticates a user and generates JWT tokens.
     * 
     * @param request The login request containing email and password
     * @return LoginResponse containing JWT tokens and user information
     * @throws InvalidCredentialsException if authentication fails
     */
    @Override @Transactional
    public LoginResponse login( LoginRequest request )
    {
        log.info( LogConstants.LOG_ATTEMPTING_LOGIN, request.getEmail() );
        try
        {
            // Authenticate user credentials
            log.debug( LogConstants.LOG_AUTHENTICATING_USER_SPRING_SECURITY );
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( request.getEmail(),
                                                                                         request.getPassword() ) );
            log.debug( LogConstants.LOG_USER_AUTHENTICATION_SUCCESSFUL, request.getEmail() );
            // Retrieve user from database using utility
            User user = userHelper.findUserByEmailOrThrow( request.getEmail(), ApiConstants.OPERATION_AUTHENTICATION );
            // Check if user account is active using utility
            userHelper.validateUserActive( user, ApiConstants.OPERATION_LOGIN );
            // Generate JWT tokens using utility
            log.debug( LogConstants.LOG_GENERATING_JWT_TOKENS, user.getEmail() );
            String accessToken = jwtUtility.generateAccessToken( user );
            String refreshToken = generateAndSaveRefreshToken( user, Boolean.TRUE.equals( request.getRememberMe() ) );
            log.info( LogConstants.LOG_LOGIN_SUCCESSFUL, request.getEmail(), user.getId() );
            // Build response using utility
            LoginResponse response = jwtUtility.buildLoginResponse( user, accessToken, refreshToken );
            response.setSuccess( true );
            log.debug( LogConstants.LOG_LOGIN_RESPONSE_CREATED, jwtUtility.getJwtExpiration() );
            return response;
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.LOG_LOGIN_FAILED, request.getEmail(), ex.getMessage() );
            throw new InvalidCredentialsException( ApiConstants.MESSAGE_INVALID_EMAIL_OR_PASSWORD );
        }
    }

    /**
     * Refreshes an access token using a valid refresh token.
     * Implements token rotation for enhanced security.
     * 
     * @param refreshToken The refresh token to validate and use
     * @return LoginResponse containing new JWT tokens
     * @throws InvalidCredentialsException if refresh token is invalid or expired
     */
    @Override @Transactional
    public LoginResponse refreshToken( String refreshToken )
    {
        log.info( LogConstants.LOG_PROCESSING_REFRESH_TOKEN_REQUEST );
        // Validate refresh token exists in database
        RefreshToken token = refreshTokenRepository.findByToken( refreshToken ).orElseThrow( () -> {
            log.warn( LogConstants.LOG_REFRESH_TOKEN_NOT_FOUND, tokenUtility.maskToken( refreshToken ) );
            return new InvalidCredentialsException( ApiConstants.MESSAGE_INVALID_REFRESH_TOKEN );
        } );
        // Check token expiration using modern time API
        if ( token.getExpiryDate().isBefore( Instant.now() ) )
        {
            log.warn( LogConstants.LOG_REFRESH_TOKEN_EXPIRED, token.getUser().getEmail(), token.getId() );
            refreshTokenRepository.delete( token );
            throw new InvalidCredentialsException( ApiConstants.MESSAGE_REFRESH_TOKEN_EXPIRED );
        }
        User user = token.getUser();
        log.debug( LogConstants.LOG_REFRESH_TOKEN_VALIDATED, user.getEmail() );
        // Remove old refresh token (token rotation)
        refreshTokenRepository.delete( token );
        log.debug( LogConstants.LOG_OLD_REFRESH_TOKEN_REMOVED, user.getEmail() );
        // Generate new tokens using utility
        String newAccessToken = jwtUtility.generateAccessToken( user );
        // For refresh token rotation, we maintain the same session duration policy
        // Since we don't store "rememberMe" state in DB, we'll default to standard expiry for rotated tokens
        // OR we could check if the old token had > 7 days remaining, but simpler to just standard refresh
        // Improvement: Can pass rememberMe state in token or DB if needed strictly.
        // For now, let's keep it simple: refreshed tokens get standard 7 days unless we change policy.
        // Actually, if a user selected 30 days, they expect it to stay.
        // Let's stick to standard refresh for rotation to avoid infinite 30-day chains without re-login?
        // Standard practice: Refresh token rotation often resets the clock.
        // Let's assume standard expiry for rotated tokens for security, or maybe 30 days if we want to be nice.
        // Given we don't have the flag, we'll use default (false).
        String newRefreshToken = generateAndSaveRefreshToken( user, false );
        log.info( LogConstants.LOG_TOKEN_REFRESH_SUCCESSFUL, user.getEmail() );
        // Build response using utility
        return jwtUtility.buildLoginResponse( user, newAccessToken, newRefreshToken );
    }

    /**
     * Logs out a user by invalidating their refresh token.
     * 
     * @param refreshToken The refresh token to invalidate
     * @throws InvalidCredentialsException if refresh token is not found
     */
    @Override @Transactional
    public void logout( String refreshToken )
    {
        log.info( LogConstants.LOG_PROCESSING_LOGOUT_REQUEST );
        // Find and remove refresh token
        RefreshToken token = refreshTokenRepository.findByToken( refreshToken ).orElseThrow( () -> {
            log.warn( LogConstants.LOG_LOGOUT_INVALID_REFRESH_TOKEN, tokenUtility.maskToken( refreshToken ) );
            return new InvalidCredentialsException( ApiConstants.MESSAGE_INVALID_REFRESH_TOKEN );
        } );
        String userEmail = token.getUser().getEmail();
        refreshTokenRepository.delete( token );
        log.info( LogConstants.LOG_USER_LOGGED_OUT_SUCCESSFULLY, userEmail );
    }

    @Override
    public LoginResponse.UserInfo getCurrentUser()
    {
        log.info( LogConstants.LOG_PROCESSING_GET_CURRENT_USER_REQUEST );
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByEmailOrThrow( email, "get current user" );
        return new LoginResponse.UserInfo( user.getId(),
                                           user.getUsername(),
                                           user.getEmail(),
                                           user.getFirstName(),
                                           user.getLastName(),
                                           user.getVerified() );
    }

    /**
     * Initiates password reset process by generating a reset token.
     * 
     * @param request The password reset request containing user email
     * @throws UserNotFoundException if user is not found
     */
    @Override @Transactional
    public void requestPasswordReset( PasswordResetRequest request )
    {
        log.info( LogConstants.LOG_PROCESSING_PASSWORD_RESET_REQUEST, request.getEmail() );
        // Find user by email using utility
        User user = userHelper.findUserByEmailOrThrow( request.getEmail(), "password reset" );
        // Generate reset token using utility
        String resetToken = tokenUtility.generateToken();
        Instant expiryTime = tokenUtility.generatePasswordResetExpiry();
        // Update user using utility
        userHelper.updateUser( user, u -> {
            u.setResetToken( resetToken );
            u.setResetTokenExpiry( expiryTime );
        } );
        log.info( LogConstants.LOG_PASSWORD_RESET_TOKEN_GENERATED, request.getEmail(), expiryTime );
        // TODO: Send email with reset token
        log.debug( LogConstants.LOG_PASSWORD_RESET_EMAIL_SHOULD_BE_SENT, request.getEmail() );
    }

    /**
     * Resets user password using a valid reset token.
     * 
     * @param token The password reset token
     * @param newPassword The new password to set
     * @throws InvalidCredentialsException if token is invalid or expired
     */
    @Override @Transactional
    public void resetPassword( String token, String newPassword )
    {
        log.info( LogConstants.LOG_PROCESSING_PASSWORD_RESET_WITH_TOKEN );
        // Find user by reset token
        User user = userRepository.findByResetToken( token ).orElseThrow( () -> {
            log.warn( LogConstants.LOG_PASSWORD_RESET_INVALID_TOKEN, tokenUtility.maskToken( token ) );
            return new InvalidCredentialsException( ApiConstants.MESSAGE_INVALID_RESET_TOKEN );
        } );
        // Check token expiration using utility
        if ( tokenUtility.isTokenExpired( user.getResetTokenExpiry() ) )
        {
            log.warn( LogConstants.LOG_PASSWORD_RESET_TOKEN_EXPIRED, user.getEmail(), user.getResetTokenExpiry() );
            throw new InvalidCredentialsException( ApiConstants.MESSAGE_RESET_TOKEN_EXPIRED );
        }
        // Update password using utility
        userHelper.updateUser( user, u -> {
            u.setPassword( passwordEncoder.encode( newPassword ) );
            u.setResetToken( null );
            u.setResetTokenExpiry( null );
        } );
        // Invalidate all refresh tokens for this user
        refreshTokenRepository.deleteByUser( user );
        log.info( LogConstants.LOG_PASSWORD_RESET_SUCCESSFUL, user.getEmail() );
        log.debug( LogConstants.LOG_ALL_REFRESH_TOKENS_INVALIDATED, user.getEmail() );
    }

    /**
     * Generates and saves a refresh token for the user.
     * 
     * @param user The user to generate refresh token for
     * @param rememberMe Whether to assign long-lived expiry
     * @return The generated refresh token string
     */
    private String generateAndSaveRefreshToken( User user, boolean rememberMe )
    {
        log.debug( LogConstants.LOG_GENERATING_REFRESH_TOKEN, user.getEmail() );
        String token = tokenUtility.generateToken();
        Instant expiryDate = tokenUtility.generateRefreshTokenExpiry( rememberMe );
        RefreshToken refreshToken = RefreshToken.builder().token( token ).user( user ).expiryDate( expiryDate ).build();
        refreshTokenRepository.save( refreshToken );
        log.debug( LogConstants.LOG_REFRESH_TOKEN_CREATED, user.getEmail(), expiryDate );
        return token;
    }
}
