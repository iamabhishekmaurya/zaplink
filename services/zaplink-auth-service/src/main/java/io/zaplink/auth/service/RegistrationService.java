package io.zaplink.auth.service;

import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.dto.response.UserRegistrationResponse;

public interface RegistrationService
{
    UserRegistrationResponse registerUser( UserRegistrationRequest request );

    void verifyEmail( String token );

    void resendVerificationEmail( String email );
}
