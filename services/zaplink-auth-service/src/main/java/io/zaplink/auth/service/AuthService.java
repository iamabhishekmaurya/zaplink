package io.zaplink.auth.service;

import io.zaplink.auth.dto.request.LoginRequest;
import io.zaplink.auth.dto.request.PasswordResetRequest;
import io.zaplink.auth.dto.response.LoginResponse;

public interface AuthService
{
    LoginResponse login( LoginRequest request );

    LoginResponse refreshToken( String refreshToken );

    void logout( String refreshToken );

    void requestPasswordReset( PasswordResetRequest request );

    void resetPassword( String token, String newPassword );
}
