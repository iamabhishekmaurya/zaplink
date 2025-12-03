package io.zaplink.auth.common.exception;

import io.zaplink.auth.common.constants.ApiConstants;

public class UserNotFoundException
    extends
    AuthException
{
    public UserNotFoundException( String message )
    {
        super( message, ApiConstants.ERROR_USER_NOT_FOUND );
    }

    public UserNotFoundException( String message, Throwable cause )
    {
        super( message, ApiConstants.ERROR_USER_NOT_FOUND, cause );
    }
}
