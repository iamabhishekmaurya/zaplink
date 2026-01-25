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
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_AUTH_ERROR );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ex.getErrorCode() );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException( UserAlreadyExistsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_USER_ALREADY_EXISTS_EXCEPTION, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.CONFLICT, ex.getMessage() );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_USER_ALREADY_EXISTS );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ex.getErrorCode() );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException( UserNotFoundException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_USER_NOT_FOUND_EXCEPTION, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, ex.getMessage() );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_USER_NOT_FOUND );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ex.getErrorCode() );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentialsException( InvalidCredentialsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_INVALID_CREDENTIALS_EXCEPTION, traceId, ex.getMessage(), ex.getErrorCode() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.UNAUTHORIZED, ex.getMessage() );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_INVALID_CREDENTIALS );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ex.getErrorCode() );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException( AuthenticationException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_AUTHENTICATION_EXCEPTION, traceId, ex.getMessage() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.UNAUTHORIZED,
                                                                        ApiConstants.MESSAGE_AUTHENTICATION_FAILED );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_AUTH_FAILED );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ApiConstants.ERROR_AUTHENTICATION_FAILED );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException( BadCredentialsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_BAD_CREDENTIALS_EXCEPTION, traceId, ex.getMessage() );
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail( HttpStatus.UNAUTHORIZED, ApiConstants.MESSAGE_INVALID_EMAIL_OR_PASSWORD );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_BAD_CREDENTIALS );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ApiConstants.ERROR_INVALID_CREDENTIALS );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException( AccessDeniedException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_ACCESS_DENIED_EXCEPTION, traceId, ex.getMessage() );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.FORBIDDEN,
                                                                        ApiConstants.MESSAGE_ACCESS_DENIED );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_ACCESS_DENIED );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ApiConstants.ERROR_ACCESS_DENIED );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
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
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_VALIDATION_FAILED );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ApiConstants.ERROR_VALIDATION_ERROR );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERRORS, errors );
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException( Exception ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_UNEXPECTED_EXCEPTION_OCCURRED, traceId, ex.getMessage(), ex );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.INTERNAL_SERVER_ERROR,
                                                                        ApiConstants.MESSAGE_UNEXPECTED_ERROR );
        problemDetail.setTitle( ApiConstants.PROBLEM_TITLE_INTERNAL_ERROR );
        problemDetail.setType( URI.create( ApiConstants.PROBLEM_TYPE_DEFAULT ) );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_ERROR_CODE, ApiConstants.ERROR_INTERNAL_SERVER_ERROR );
        problemDetail.setProperty( ApiConstants.PROBLEM_PROPERTY_TRACE_ID, traceId );
        return problemDetail;
    }
}
