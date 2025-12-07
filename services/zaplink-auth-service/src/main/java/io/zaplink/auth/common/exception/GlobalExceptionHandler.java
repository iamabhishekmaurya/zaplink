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
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.trace.TraceContext;
import io.zaplink.auth.dto.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<BaseResponse> handleAuthException( AuthException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_AUTH_EXCEPTION_OCCURRED, 
                   traceId, ex.getMessage(), ex.getErrorCode() );
        
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<BaseResponse> handleUserAlreadyExistsException( UserAlreadyExistsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_USER_ALREADY_EXISTS_EXCEPTION, 
                   traceId, ex.getMessage(), ex.getErrorCode() );
        
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.CONFLICT ).body( response );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponse> handleUserNotFoundException( UserNotFoundException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_USER_NOT_FOUND_EXCEPTION, 
                   traceId, ex.getMessage(), ex.getErrorCode() );
        
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( response );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<BaseResponse> handleInvalidCredentialsException( InvalidCredentialsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_INVALID_CREDENTIALS_EXCEPTION, 
                   traceId, ex.getMessage(), ex.getErrorCode() );
        
        BaseResponse response = BaseResponse.error( ex.getMessage(), ex.getErrorCode() );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse> handleAuthenticationException( AuthenticationException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_AUTHENTICATION_EXCEPTION, 
                   traceId, ex.getMessage() );
        
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_AUTHENTICATION_FAILED,
                                                    ApiConstants.ERROR_AUTHENTICATION_FAILED );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse> handleBadCredentialsException( BadCredentialsException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_BAD_CREDENTIALS_EXCEPTION, 
                   traceId, ex.getMessage() );
        
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_INVALID_EMAIL_OR_PASSWORD,
                                                    ApiConstants.ERROR_INVALID_CREDENTIALS );
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( response );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException( AccessDeniedException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_ACCESS_DENIED_EXCEPTION, 
                   traceId, ex.getMessage() );
        
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_ACCESS_DENIED,
                                                    ApiConstants.ERROR_ACCESS_DENIED );
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( response );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions( MethodArgumentNotValidException ex )
    {
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_VALIDATION_EXCEPTION_OCCURRED, 
                   traceId, ex.getBindingResult().getAllErrors().size() );
        
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
        String traceId = TraceContext.getTraceId();
        log.error( LogConstants.LOG_UNEXPECTED_EXCEPTION_OCCURRED, 
                   traceId, ex.getMessage(), ex );
        
        BaseResponse response = BaseResponse.error( ApiConstants.MESSAGE_UNEXPECTED_ERROR,
                                                    ApiConstants.ERROR_INTERNAL_SERVER_ERROR );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( response );
    }
}
