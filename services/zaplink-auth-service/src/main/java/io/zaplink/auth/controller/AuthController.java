package io.zaplink.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.constants.StatusConstants;
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
@Slf4j @RestController @RequiredArgsConstructor @RequestMapping("/auth") @Tag(name = ApiConstants.TAG_AUTHENTICATION, description = ApiConstants.TAG_AUTHENTICATION_DESC)
public class AuthController
{
    private final RegistrationService registrationService;
    private final AuthService         authService;
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) @Operation(summary = ApiConstants.AUTH_REGISTER_SUMMARY, description = ApiConstants.AUTH_REGISTER_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_201_CREATED, description = ApiConstants.RESPONSE_201_USER_REGISTERED, content = @Content(schema = @Schema(implementation = UserRegistrationResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ApiConstants.RESPONSE_400_INVALID_INPUT),
      @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ApiConstants.RESPONSE_409_USER_ALREADY_EXISTS) })
    public UserRegistrationResponse registerUser( @Valid @RequestBody UserRegistrationRequest request )
    {
        log.info( LogConstants.LOG_CREATING_NEW_USER_ACCOUNT, request.email() );
        UserRegistrationResponse response = registrationService.registerUser( request );
        log.info( LogConstants.LOG_USER_REGISTERED_SUCCESSFULLY, response.getUserId(), response.getEmail(),
                  response.getUsername() );
        return response;
    }

    @PostMapping("/login") @Operation(summary = ApiConstants.AUTH_LOGIN_SUMMARY, description = ApiConstants.AUTH_LOGIN_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_LOGIN_SUCCESSFUL, content = @Content(schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_401_UNAUTHORIZED, description = ApiConstants.RESPONSE_401_UNAUTHORIZED) })
    public LoginResponse login( @Valid @RequestBody LoginRequest request )
    {
        log.info( LogConstants.LOG_ATTEMPTING_LOGIN, request.email() );
        LoginResponse response = authService.login( request );
        log.info( LogConstants.LOG_LOGIN_SUCCESSFUL, response.getUserInfo().getEmail(),
                  response.getUserInfo().getId() );
        return response;
    }

    @GetMapping("/me") @Operation(summary = ApiConstants.AUTH_GET_CURRENT_USER_SUMMARY, description = ApiConstants.AUTH_GET_CURRENT_USER_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_USER_INFO_RETRIEVED, content = @Content(schema = @Schema(implementation = LoginResponse.UserInfo.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_401_UNAUTHORIZED, description = ApiConstants.RESPONSE_401_UNAUTHORIZED) })
    public LoginResponse.UserInfo getCurrentUser()
    {
        log.info( LogConstants.LOG_PROCESSING_GET_CURRENT_USER_REQUEST );
        return authService.getCurrentUser();
    }

    @PostMapping("/refresh") @Operation(summary = ApiConstants.AUTH_REFRESH_TOKEN_SUMMARY, description = ApiConstants.AUTH_REFRESH_TOKEN_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_TOKEN_REFRESHED, content = @Content(schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_401_UNAUTHORIZED, description = ApiConstants.RESPONSE_401_INVALID_REFRESH_TOKEN) })
    public LoginResponse refreshToken( @Parameter(description = ApiConstants.PARAM_REFRESH_TOKEN) @RequestParam("refreshToken") String refreshToken )
    {
        log.info( LogConstants.LOG_PROCESSING_REFRESH_TOKEN_REQUEST );
        LoginResponse response = authService.refreshToken( refreshToken );
        log.info( LogConstants.LOG_TOKEN_REFRESH_SUCCESSFUL, response.getUserInfo().getEmail() );
        return response;
    }

    @PostMapping("/logout") @Operation(summary = ApiConstants.AUTH_LOGOUT_SUMMARY, description = ApiConstants.AUTH_LOGOUT_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_LOGOUT_SUCCESSFUL, content = @Content(schema = @Schema(implementation = BaseResponse.class))) })
    public BaseResponse logout( @Parameter(description = ApiConstants.PARAM_REFRESH_TOKEN) @RequestParam("refreshToken") String refreshToken )
    {
        log.info( LogConstants.LOG_PROCESSING_LOGOUT_REQUEST );
        authService.logout( refreshToken );
        log.info( LogConstants.LOG_USER_LOGGED_OUT_SUCCESSFULLY, LogConstants.PARAM_USER );
        return BaseResponse.success( ApiConstants.MESSAGE_LOGOUT_SUCCESSFUL );
    }

    @PostMapping("/verify-email") @Operation(summary = ApiConstants.AUTH_VERIFY_EMAIL_SUMMARY, description = ApiConstants.AUTH_VERIFY_EMAIL_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_EMAIL_VERIFIED, content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ApiConstants.RESPONSE_400_INVALID_TOKEN) })
    public BaseResponse verifyEmail( @Parameter(description = ApiConstants.PARAM_VERIFICATION_TOKEN) @RequestParam("token") String token )
    {
        log.info( LogConstants.LOG_PROCESSING_EMAIL_VERIFICATION );
        registrationService.verifyEmail( token );
        log.info( LogConstants.LOG_EMAIL_VERIFIED_SUCCESSFULLY, LogConstants.PARAM_USER, LogConstants.PARAM_ID );
        return BaseResponse.success( ApiConstants.MESSAGE_EMAIL_VERIFIED_SUCCESSFULLY );
    }

    @PostMapping("/resend-verification") @Operation(summary = ApiConstants.AUTH_RESEND_VERIFICATION_SUMMARY, description = ApiConstants.AUTH_RESEND_VERIFICATION_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_VERIFICATION_EMAIL_SENT, content = @Content(schema = @Schema(implementation = BaseResponse.class))) })
    public BaseResponse resendVerificationEmail( @Parameter(description = ApiConstants.PARAM_EMAIL) @RequestParam("email") String email )
    {
        registrationService.resendVerificationEmail( email );
        return BaseResponse.success( ApiConstants.MESSAGE_VERIFICATION_EMAIL_SENT );
    }

    @PostMapping("/request-password-reset") @Operation(summary = ApiConstants.AUTH_REQUEST_PASSWORD_RESET_SUMMARY, description = ApiConstants.AUTH_REQUEST_PASSWORD_RESET_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_PASSWORD_RESET_EMAIL_SENT, content = @Content(schema = @Schema(implementation = BaseResponse.class))) })
    public BaseResponse requestPasswordReset( @Valid @RequestBody PasswordResetRequest request )
    {
        log.info( LogConstants.LOG_PROCESSING_PASSWORD_RESET_REQUEST, request.email() );
        authService.requestPasswordReset( request );
        log.info( LogConstants.LOG_PASSWORD_RESET_EMAIL_SHOULD_BE_SENT, request.email() );
        return BaseResponse.success( ApiConstants.MESSAGE_PASSWORD_RESET_EMAIL_SENT );
    }

    @PostMapping("/reset-password") @Operation(summary = ApiConstants.AUTH_RESET_PASSWORD_SUMMARY, description = ApiConstants.AUTH_RESET_PASSWORD_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_PASSWORD_RESET_SUCCESSFUL, content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ApiConstants.RESPONSE_400_INVALID_TOKEN) })
    public BaseResponse resetPassword( @Parameter(description = ApiConstants.PARAM_RESET_TOKEN) @RequestParam("token") String token,
                                       @Parameter(description = ApiConstants.PARAM_NEW_PASSWORD) @RequestParam("newPassword") String newPassword )
    {
        log.info( LogConstants.LOG_PROCESSING_PASSWORD_RESET_WITH_TOKEN );
        authService.resetPassword( token, newPassword );
        log.info( LogConstants.LOG_PASSWORD_RESET_SUCCESSFUL, LogConstants.PARAM_USER );
        return BaseResponse.success( ApiConstants.MESSAGE_PASSWORD_RESET_SUCCESSFULLY );
    }
}
