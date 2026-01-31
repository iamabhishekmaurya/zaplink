package io.zaplink.manager.common.exception;

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

import io.zaplink.manager.dto.error.ErrorResponse;
import io.zaplink.manager.dto.error.FieldError;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler
{
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
                                                                 "Validation failed",
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 fieldErrors );
                return ResponseEntity.badRequest().body( errorResponse );
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGlobalException( Exception ex, WebRequest request )
        {
                log.error( "Unhandled exception occurred at path: {}", request.getDescription( false ), ex );
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
                                                                 "Internal server error",
                                                                 request.getDescription( false ).replace( "uri=", "" ),
                                                                 List.of( new FieldError( "global",
                                                                                          ex.getMessage(),
                                                                                          null ) ) );
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( errorResponse );
        }
}
