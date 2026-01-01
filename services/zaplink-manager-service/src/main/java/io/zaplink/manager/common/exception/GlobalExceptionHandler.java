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

@RestControllerAdvice
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
                List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map( error -> FieldError
                                .builder().field( error.getField() ).message( error.getDefaultMessage() )
                                .rejectedValue( error.getRejectedValue() != null ? error.getRejectedValue().toString()
                                                                                 : null )
                                .build() ).collect( Collectors.toList() );
                ErrorResponse errorResponse = ErrorResponse.builder().timestamp( LocalDateTime.now().toString() )
                                .status( HttpStatus.BAD_REQUEST.name() ).message( "Validation failed" )
                                .path( request.getDescription( false ).replace( "uri=", "" ) )
                                .fieldErrors( fieldErrors ).build();
                return ResponseEntity.badRequest().body( errorResponse );
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGlobalException( Exception ex, WebRequest request )
        {
                // Check if request wants OpenMetrics format (for Prometheus)
                String acceptHeader = request.getHeader( HttpHeaders.ACCEPT );
                if ( acceptHeader != null && acceptHeader.contains( "application/openmetrics-text" ) )
                {
                        // Return plain text error for Prometheus scraping
                        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                        .contentType( MediaType.TEXT_PLAIN )
                                        .body( "# ERROR: " + ex.getMessage() + "\n" );
                }
                ErrorResponse errorResponse = ErrorResponse.builder().timestamp( LocalDateTime.now().toString() )
                                .status( HttpStatus.INTERNAL_SERVER_ERROR.name() ).message( "Internal server error" )
                                .path( request.getDescription( false ).replace( "uri=", "" ) )
                                .fieldErrors( List.of( FieldError.builder().field( "global" ).message( ex.getMessage() )
                                                .rejectedValue( null ).build() ) )
                                .build();
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( errorResponse );
        }
}
