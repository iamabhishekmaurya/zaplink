package io.zaplink.social.common.handler.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler
    extends
    ResponseEntityExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandledException( Exception e )
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.INTERNAL_SERVER_ERROR,
                                                                        e.getMessage() );
        problemDetail.setTitle( "Internal Server Error" );
        problemDetail.setProperty( "timestamp", Instant.now() );
        return problemDetail;
    }

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
