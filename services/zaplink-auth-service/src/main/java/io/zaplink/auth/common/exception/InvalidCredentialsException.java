package io.zaplink.auth.common.exception;

import io.zaplink.auth.common.constants.ApiConstants;

public class InvalidCredentialsException
    extends
    AuthException
{
    public InvalidCredentialsException( String message )
    {
        super( message, ApiConstants.ERROR_INVALID_CREDENTIALS );
    }

    public InvalidCredentialsException( String message, Throwable cause )
    {
        super( message, ApiConstants.ERROR_INVALID_CREDENTIALS, cause );
    }
}
