package io.zaplink.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.dto.request.LoginRequest;
import io.zaplink.auth.dto.request.PasswordResetRequest;
import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.dto.response.BaseResponse;
import io.zaplink.auth.dto.response.LoginResponse;
import io.zaplink.auth.dto.response.UserRegistrationResponse;
import io.zaplink.auth.service.AuthService;
import io.zaplink.auth.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for authentication operations.
 * Provides endpoints for user registration, login, token management, and password operations.
 * 
 * Base URL: /auth
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j @RestController @RequestMapping("${api.base-path}/auth") @RequiredArgsConstructor
public class AuthController
{
    private final RegistrationService registrationService;
    private final AuthService         authService;
    /**
     * Registers a new user in the system.
     * 
     * @param request The user registration request with user details
     * @return UserRegistrationResponse with created user information
     */
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponse registerUser( @Valid @RequestBody UserRegistrationRequest request )
    {
        log.info( LogConstants.LOG_CREATING_NEW_USER_ACCOUNT, request.getEmail() );
        UserRegistrationResponse response = registrationService.registerUser( request );
        log.info( LogConstants.LOG_USER_REGISTERED_SUCCESSFULLY, response.getUserId(), response.getEmail(),
                  response.getUsername() );
        return response;
    }

    /**
     * Authenticates a user and returns JWT tokens.
     * 
     * @param request The login request containing email and password
     * @return LoginResponse containing JWT access and refresh tokens
     */
    @PostMapping("/login")
    public LoginResponse login( @Valid @RequestBody LoginRequest request )
    {
        log.info( LogConstants.LOG_ATTEMPTING_LOGIN, request.getEmail() );
        LoginResponse response = authService.login( request );
        log.info( LogConstants.LOG_LOGIN_SUCCESSFUL, response.getUserInfo().getEmail(),
                  response.getUserInfo().getId() );
        return response;
    }

    /**
     * Retrieves the current authenticated user's information.
     * 
     * @return UserInfo containing current user details
     */
    @GetMapping("/me")
    public LoginResponse.UserInfo getCurrentUser()
    {
        log.info( LogConstants.LOG_PROCESSING_GET_CURRENT_USER_REQUEST );
        return authService.getCurrentUser();
    }

    /**
     * Refreshes an access token using a valid refresh token.
     * Implements token rotation for enhanced security.
     * 
     * @param refreshToken The refresh token to validate and use
     * @return LoginResponse containing new JWT tokens
     */
    @PostMapping("/refresh")
    public LoginResponse refreshToken( @RequestParam("refreshToken") String refreshToken )
    {
        log.info( LogConstants.LOG_PROCESSING_REFRESH_TOKEN_REQUEST );
        LoginResponse response = authService.refreshToken( refreshToken );
        log.info( LogConstants.LOG_TOKEN_REFRESH_SUCCESSFUL, response.getUserInfo().getEmail() );
        return response;
    }

    /**
     * Logs out a user by invalidating their refresh token.
     * 
     * @param refreshToken The refresh token to invalidate
     * @return BaseResponse indicating logout success
     */
    @PostMapping("/logout")
    public BaseResponse logout( @RequestParam("refreshToken") String refreshToken )
    {
        log.info( LogConstants.LOG_PROCESSING_LOGOUT_REQUEST );
        authService.logout( refreshToken );
        log.info( LogConstants.LOG_USER_LOGGED_OUT_SUCCESSFULLY, "user" );
        return BaseResponse.success( ApiConstants.MESSAGE_LOGOUT_SUCCESSFUL );
    }

    /**
     * Verifies a user's email address using a verification token.
     * 
     * @param token The email verification token
     * @return BaseResponse indicating verification status
     */
    @PostMapping("/verify-email")
    public BaseResponse verifyEmail( @RequestParam("token") String token )
    {
        log.info( LogConstants.LOG_PROCESSING_EMAIL_VERIFICATION );
        registrationService.verifyEmail( token );
        log.info( LogConstants.LOG_EMAIL_VERIFIED_SUCCESSFULLY, "user", "ID" );
        return BaseResponse.success( ApiConstants.MESSAGE_EMAIL_VERIFIED_SUCCESSFULLY );
    }

    /**
     * Resends the verification email to a user.
     * 
     * @param email The email address to send verification to
     * @return BaseResponse indicating email send status
     */
    @PostMapping("/resend-verification")
    public BaseResponse resendVerificationEmail( @RequestParam("email") String email )
    {
        registrationService.resendVerificationEmail( email );
        return BaseResponse.success( ApiConstants.MESSAGE_VERIFICATION_EMAIL_SENT );
    }

    /**
     * Initiates password reset process by sending reset email.
     * 
     * @param request The password reset request containing user email
     * @return BaseResponse indicating reset initiation status
     */
    @PostMapping("/request-password-reset")
    public BaseResponse requestPasswordReset( @Valid @RequestBody PasswordResetRequest request )
    {
        log.info( LogConstants.LOG_PROCESSING_PASSWORD_RESET_REQUEST, request.getEmail() );
        authService.requestPasswordReset( request );
        log.info( LogConstants.LOG_PASSWORD_RESET_EMAIL_SHOULD_BE_SENT, request.getEmail() );
        return BaseResponse.success( ApiConstants.MESSAGE_PASSWORD_RESET_EMAIL_SENT );
    }

    /**
     * Resets user password using a valid reset token.
     * 
     * @param token The password reset token
     * @param newPassword The new password to set
     * @return BaseResponse indicating password reset status
     */
    @PostMapping("/reset-password")
    public BaseResponse resetPassword( @RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword )
    {
        log.info( LogConstants.LOG_PROCESSING_PASSWORD_RESET_WITH_TOKEN );
        authService.resetPassword( token, newPassword );
        log.info( LogConstants.LOG_PASSWORD_RESET_SUCCESSFUL, "user" );
        return BaseResponse.success( ApiConstants.MESSAGE_PASSWORD_RESET_SUCCESSFULLY );
    }
}
