package io.zaplink.shortner.common.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.zaplink.shortner.dto.error.ErrorResponse;
import io.zaplink.shortner.dto.error.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions( MethodArgumentNotValidException ex,
                                                                     WebRequest request )
    {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map( error -> FieldError.builder().field( error.getField() ).message( error.getDefaultMessage() )
                        .rejectedValue( error.getRejectedValue() != null ? error.getRejectedValue().toString() : null )
                        .build() )
                .collect( Collectors.toList() );
        ErrorResponse errorResponse = ErrorResponse.builder().timestamp( LocalDateTime.now().toString() )
                .status( HttpStatus.BAD_REQUEST.name() ).message( "Validation failed" )
                .path( request.getDescription( false ).replace( "uri=", "" ) ).fieldErrors( fieldErrors ).build();
        return ResponseEntity.badRequest().body( errorResponse );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException( Exception ex, WebRequest request )
    {
        ErrorResponse errorResponse = ErrorResponse.builder().timestamp( LocalDateTime.now().toString() )
                .status( HttpStatus.INTERNAL_SERVER_ERROR.name() ).message( "Internal server error" )
                .path( request.getDescription( false ).replace( "uri=", "" ) ).fieldErrors( List.of( FieldError
                        .builder().field( "global" ).message( ex.getMessage() ).rejectedValue( null ).build() ) )
                .build();
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( errorResponse );
    }
}
