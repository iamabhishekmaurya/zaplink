package io.zaplink.auth.common.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.trace.TraceContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(AuthException.class)
    public ProblemDetail handleAuthException( AuthException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_AUTH_EXCEPTION_OCCURRED, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.UNAUTHORIZED, ex.getMessage() );
        problemDetail.setTitle( "Authentication Error" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ex.getErrorCode() );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException( UserAlreadyExistsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_USER_ALREADY_EXISTS_EXCEPTION, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.CONFLICT, ex.getMessage() );
        problemDetail.setTitle( "User Already Exists" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ex.getErrorCode() );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException( UserNotFoundException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_USER_NOT_FOUND_EXCEPTION, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, ex.getMessage() );
        problemDetail.setTitle( "User Not Found" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ex.getErrorCode() );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentialsException( InvalidCredentialsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_INVALID_CREDENTIALS_EXCEPTION, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.UNAUTHORIZED, ex.getMessage() );
        problemDetail.setTitle( "Invalid Credentials" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ex.getErrorCode() );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException( AuthenticationException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_AUTHENTICATION_EXCEPTION, traceId, ex.getMessage() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.UNAUTHORIZED,
                                                                        ApiConstants.MESSAGE_AUTHENTICATION_FAILED );
        problemDetail.setTitle( "Authentication Failed" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ApiConstants.ERROR_AUTHENTICATION_FAILED );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException( BadCredentialsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_BAD_CREDENTIALS_EXCEPTION, traceId, ex.getMessage() );
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail( HttpStatus.UNAUTHORIZED, ApiConstants.MESSAGE_INVALID_EMAIL_OR_PASSWORD );
        problemDetail.setTitle( "Bad Credentials" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ApiConstants.ERROR_INVALID_CREDENTIALS );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException( AccessDeniedException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_ACCESS_DENIED_EXCEPTION, traceId, ex.getMessage() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.FORBIDDEN,
                                                                        ApiConstants.MESSAGE_ACCESS_DENIED );
        problemDetail.setTitle( "Access Denied" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ApiConstants.ERROR_ACCESS_DENIED );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions( MethodArgumentNotValidException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_VALIDATION_EXCEPTION_OCCURRED, traceId,
                   ex.getBindingResult().getAllErrors().size() );
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach( ( error ) -> {
            String fieldName = ( (FieldError) error ).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put( fieldName, errorMessage );
        } );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST,
                                                                        ApiConstants.MESSAGE_VALIDATION_FAILED );
        problemDetail.setTitle( "Validation Failed" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ApiConstants.ERROR_VALIDATION_ERROR );
        problemDetail.setProperty( "traceId", traceId );
        problemDetail.setProperty( "errors", errors );
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException( Exception ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_UNEXPECTED_EXCEPTION_OCCURRED, traceId, ex.getMessage(), ex );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.INTERNAL_SERVER_ERROR,
                                                                        ApiConstants.MESSAGE_UNEXPECTED_ERROR );
        problemDetail.setTitle( "Internal Server Error" );
        problemDetail.setType( URI.create( "about:blank" ) );
        problemDetail.setProperty( "errorCode", ApiConstants.ERROR_INTERNAL_SERVER_ERROR );
        problemDetail.setProperty( "traceId", traceId );
        return problemDetail;
    }
}
