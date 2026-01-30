package io.zaplink.scheduler.common.handler.exception;

import io.zaplink.scheduler.common.constants.ApiResponseMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

/**
 * Global Exception Handler.
 * <p>
 * Centralizes exception handling across the application, converting exceptions into
 * standard {@link ProblemDetail} responses.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler
    extends
    ResponseEntityExceptionHandler
{
    /**
     * Handles unhandled exceptions (fallback).
     *
     * @param e The exception caught.
     * @return ProblemDetail with status 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandledException( Exception e )
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.INTERNAL_SERVER_ERROR,
                                                                        e.getMessage() );
        problemDetail.setTitle( ApiResponseMessages.TITLE_INTERNAL_SERVER_ERROR );
        problemDetail.setProperty( ApiResponseMessages.TIMESTAMP, Instant.now() );
        return problemDetail;
    }

    /**
     * Handles IllegalArgumentExceptions (e.g., validation errors).
     *
     * @param e The exception caught.
     * @return ProblemDetail with status 400 (Bad Request).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException( IllegalArgumentException e )
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, e.getMessage() );
        problemDetail.setTitle( ApiResponseMessages.TITLE_BAD_REQUEST );
        problemDetail.setProperty( ApiResponseMessages.TIMESTAMP, Instant.now() );
        return problemDetail;
    }
}
