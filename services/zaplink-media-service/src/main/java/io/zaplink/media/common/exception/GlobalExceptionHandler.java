package io.zaplink.media.common.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.zaplink.media.common.constants.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import io.zaplink.media.dto.error.ErrorResponse;
import io.zaplink.media.dto.error.FieldError;

/**
 * Global Exception Handler for the Media Service.
 * Centralizes error handling to ensure consistent JSON responses across the API.
 * Maps specific exceptions (like AssetNotFoundException) to HTTP status codes.
 */
@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler
{
        /**
         * Handles validation errors (e.g., @Valid annotation failures).
         * @return 400 Bad Request with a list of field validation errors.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidationExceptions( MethodArgumentNotValidException ex,
                                                                  WebRequest request )
        {
                // Check if request wants OpenMetrics format (for Prometheus)
                String acceptHeader = request.getHeader( HttpHeaders.ACCEPT );
                if ( acceptHeader != null && acceptHeader.contains( "application/openmetrics-text" ) )
                {
                        // Return plain text error for Prometheus scraping
                        return ResponseEntity.badRequest().contentType( MediaType.TEXT_PLAIN )
                                        .body( ExceptionConstants.ERR_PREFIX_VALIDATION + ex.getMessage() + "\n" );
                }
                List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                                .map( error -> new FieldError( error.getField(),
                                                               error.getDefaultMessage(),
                                                               error.getRejectedValue() != null ? error
                                                                               .getRejectedValue().toString() : null ) )
                                .toList();
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.BAD_REQUEST.name(),
                                                                 ExceptionConstants.ERR_VALIDATION_FAILED,
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 fieldErrors );
                return ResponseEntity.badRequest().contentType( MediaType.APPLICATION_JSON ).body( errorResponse );
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGlobalException( Exception ex, WebRequest request )
        {
                log.error( "Unhandled exception in Media Service", ex );
                // Check if request wants OpenMetrics format (for Prometheus)
                String acceptHeader = request.getHeader( HttpHeaders.ACCEPT );
                if ( acceptHeader != null && acceptHeader.contains( "application/openmetrics-text" ) )
                {
                        // Return plain text error for Prometheus scraping
                        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                        .contentType( MediaType.TEXT_PLAIN )
                                        .body( ExceptionConstants.ERR_PREFIX_ERROR + ex.getMessage() + "\n" );
                }
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                                                 ExceptionConstants.ERR_INTERNAL_SERVER_ERROR,
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of( new FieldError( "global",
                                                                                          ex.getMessage(),
                                                                                          null ) ) );
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                .contentType( MediaType.APPLICATION_JSON ).body( errorResponse );
        }

        @ExceptionHandler(AssetNotFoundException.class)
        public ResponseEntity<Object> handleAssetNotFoundException( AssetNotFoundException ex, WebRequest request )
        {
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.NOT_FOUND.name(),
                                                                 ex.getMessage(),
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of() );
                return ResponseEntity.status( HttpStatus.NOT_FOUND ).contentType( MediaType.APPLICATION_JSON )
                                .body( errorResponse );
        }

        @ExceptionHandler(StorageException.class)
        public ResponseEntity<Object> handleStorageException( StorageException ex, WebRequest request )
        {
                ErrorResponse errorResponse = new ErrorResponse( LocalDateTime.now().toString(),
                                                                 HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                                                 ex.getMessage(),
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of() );
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                .contentType( MediaType.APPLICATION_JSON ).body( errorResponse );
        }
}
