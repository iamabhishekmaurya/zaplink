package io.zaplink.auth.common.exception;

import io.zaplink.auth.common.constants.ApiConstants;

public class UserAlreadyExistsException
    extends
    AuthException
{
    public UserAlreadyExistsException( String message )
    {
        super( message, ApiConstants.ERROR_USER_ALREADY_EXISTS );
    }

    public UserAlreadyExistsException( String message, Throwable cause )
    {
        super( message, ApiConstants.ERROR_USER_ALREADY_EXISTS, cause );
    }
}
