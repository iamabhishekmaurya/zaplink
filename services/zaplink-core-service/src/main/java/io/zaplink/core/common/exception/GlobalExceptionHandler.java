package io.zaplink.core.common.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.MessageConstants;
import io.zaplink.core.dto.error.ErrorResponse;
import io.zaplink.core.dto.error.FieldError;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for Core Service.
 * Provides centralized error handling with consistent error responses.
 * Enhanced with comprehensive logging and structured error responses.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler
{
        /**
         * Handles validation exceptions for request body validation failures.
         * 
         * @param ex The validation exception
         * @param request The web request
         * @return Error response with validation details
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidationExceptions( MethodArgumentNotValidException ex,
                                                                  WebRequest request )
        {
                log.warn( "Validation failed: {}", ex.getBindingResult().getAllErrors() );
                // Check if request wants OpenMetrics format (for Prometheus)
                String acceptHeader = request.getHeader( HttpHeaders.ACCEPT );
                if ( acceptHeader != null && acceptHeader.contains( "application/openmetrics-text" ) )
                {
                        // Return plain text error for Prometheus scraping
                        return ResponseEntity.badRequest().contentType( MediaType.TEXT_PLAIN )
                                        .body( "# VALIDATION ERROR: " + ex.getMessage() + "\n" );
                }
                List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                                .map( error -> new FieldError( error.getField(),
                                                               error.getDefaultMessage(),
                                                               error.getRejectedValue() != null ? error
                                                                               .getRejectedValue().toString() : null ) )
                                .collect( Collectors.toList() );
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.BAD_REQUEST.name(),
                                                                 ErrorConstant.ERROR_VALIDATION_FAILED,
                                                                 request.getDescription( false )
                                                                                 .replace( MessageConstants.SEPARATOR_URI_EQUALS,
                                                                                           "" ),
                                                                 fieldErrors );
                return ResponseEntity.badRequest().contentType( MediaType.APPLICATION_JSON ).body( errorResponse );
        }

        /**
         * Handles resource not found exceptions.
         * 
         * @param ex The resource not found exception
         * @param request The web request
         * @return Error response
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Object> handleResourceNotFoundException( ResourceNotFoundException ex,
                                                                       WebRequest request )
        {
                log.error( "Resource not found: {}", ex.getMessage() );
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.NOT_FOUND.name(),
                                                                 ex.getMessage(),
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of() );
                return ResponseEntity.status( HttpStatus.NOT_FOUND ).contentType( MediaType.APPLICATION_JSON )
                                .body( errorResponse );
        }

        /**
         * Handles business logic exceptions.
         * 
         * @param ex The business exception
         * @param request The web request
         * @return Error response
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<Object> handleBusinessException( BusinessException ex, WebRequest request )
        {
                log.error( "Business rule violation: {}", ex.getMessage() );
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.UNPROCESSABLE_ENTITY.name(),
                                                                 ex.getMessage(),
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of() );
                return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY )
                                .contentType( MediaType.APPLICATION_JSON ).body( errorResponse );
        }

        /**
         * Handles illegal argument exceptions.
         * 
         * @param ex The illegal argument exception
         * @param request The web request
         * @return Error response
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Object> handleIllegalArgumentException( IllegalArgumentException ex, WebRequest request )
        {
                log.warn( "Illegal argument: {}", ex.getMessage() );
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.BAD_REQUEST.name(),
                                                                 ErrorConstant.ERROR_VALIDATION_FAILED,
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of( new FieldError( "global",
                                                                                          ex.getMessage(),
                                                                                          null ) ) );
                return ResponseEntity.status( HttpStatus.BAD_REQUEST ).contentType( MediaType.APPLICATION_JSON )
                                .body( errorResponse );
        }

        /**
         * Handles all other exceptions.
         * 
         * @param ex The exception
         * @param request The web request
         * @return Error response
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGlobalException( Exception ex, WebRequest request )
        {
                log.error( "Unexpected error occurred", ex );
                // Check if request wants OpenMetrics format (for Prometheus)
                String acceptHeader = request.getHeader( HttpHeaders.ACCEPT );
                if ( acceptHeader != null && acceptHeader.contains( "application/openmetrics-text" ) )
                {
                        // Return plain text error for Prometheus scraping
                        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                        .contentType( MediaType.TEXT_PLAIN )
                                        .body( "# ERROR: " + ex.getMessage() + "\n" );
                }
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                                                 ErrorConstant.ERROR_INTERNAL_SERVER_ERROR,
                                                                 request.getDescription( false )
                                                                                 .replace( MessageConstants.SEPARATOR_URI_EQUALS,
                                                                                           "" ),
                                                                 List.of( new FieldError( MessageConstants.FIELD_GLOBAL,
                                                                                          ex.getMessage(),
                                                                                          null ) ) );
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                .contentType( MediaType.APPLICATION_JSON ).body( errorResponse );
        }
}
