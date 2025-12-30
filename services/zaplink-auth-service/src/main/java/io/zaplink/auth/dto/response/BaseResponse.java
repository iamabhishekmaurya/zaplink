package io.zaplink.auth.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base response class for all API responses.
 * Uses modern Java 17 features with SuperBuilder pattern and modern time API.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse
{
    private boolean success;
    private String  message;
    private String  errorCode;
    private Instant timestamp;
    public BaseResponse( boolean success, String message )
    {
        this.success = success;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public BaseResponse( boolean success, String message, String errorCode )
    {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    public static BaseResponse success( String message )
    {
        return new BaseResponse( true, message );
    }

    public static BaseResponse error( String message )
    {
        return new BaseResponse( false, message );
    }

    public static BaseResponse error( String message, String errorCode )
    {
        return new BaseResponse( false, message, errorCode );
    }
}
