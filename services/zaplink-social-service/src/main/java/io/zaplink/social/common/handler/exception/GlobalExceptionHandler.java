package io.zaplink.social.common.handler.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the Social Service.
 * <p>
 * Centralizes exception handling to ensure consistent error responses across the API.
 * Uses {@link ProblemDetail} (RFC 7807) for error details.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler
    extends
    ResponseEntityExceptionHandler
{
    /**
     * catch-all handler for any unhandled exceptions.
     * Returns 500 Internal Server Error.
     *
     * @param e The exception caught.
     * @return ProblemDetail with status 500.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandledException( Exception e )
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.INTERNAL_SERVER_ERROR,
                                                                        e.getMessage() );
        problemDetail.setTitle( "Internal Server Error" );
        problemDetail.setProperty( "timestamp", Instant.now() );
        return problemDetail;
    }

    /**
     * Handles bad requests (e.g., invalid input).
     * Returns 400 Bad Request.
     *
     * @param e The exception caught.
     * @return ProblemDetail with status 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException( IllegalArgumentException e )
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, e.getMessage() );
        problemDetail.setTitle( "Bad Request" );
        problemDetail.setProperty( "timestamp", Instant.now() );
        return problemDetail;
    }
    // Add more custom exceptions here as needed
}
