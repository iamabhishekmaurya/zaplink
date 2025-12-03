package io.zaplink.auth.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.dto.response.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<BaseResponse> handleAuthException( AuthException ex )
    {
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<BaseResponse> handleUserAlreadyExistsException( UserAlreadyExistsException ex )
    {
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.CONFLICT ).body( response );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponse> handleUserNotFoundException( UserNotFoundException ex )
    {
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( response );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<BaseResponse> handleInvalidCredentialsException( InvalidCredentialsException ex )
    {
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse> handleAuthenticationException( AuthenticationException ex )
    {
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_AUTHENTICATION_FAILED,
                                                    ApiConstants.ERROR_AUTHENTICATION_FAILED );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse> handleBadCredentialsException( BadCredentialsException ex )
    {
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_INVALID_EMAIL_OR_PASSWORD,
                                                    ApiConstants.ERROR_INVALID_CREDENTIALS );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException( AccessDeniedException ex )
    {
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_ACCESS_DENIED,
                                                    ApiConstants.ERROR_ACCESS_DENIED );
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( response );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions( MethodArgumentNotValidException ex )
    {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach( ( error ) -> {
            String fieldName = ( (FieldError) error ).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put( fieldName, errorMessage );
        } );
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_VALIDATION_FAILED,
                                                    ApiConstants.ERROR_VALIDATION_ERROR );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( response );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGlobalException( Exception ex )
    {
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_UNEXPECTED_ERROR,
                                                    ApiConstants.ERROR_INTERNAL_SERVER_ERROR );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( response );
    }
}
