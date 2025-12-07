package io.zaplink.notification.dto.response;

import java.time.LocalDateTime;

import io.zaplink.notification.common.constants.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized email response object for the notification service.
 * Contains comprehensive information about email sending operations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-07
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmailResponse
{
    private boolean       success;
    private String        to;
    private String        subject;
    private String        message;
    private String        errorMessage;
    private String        traceId;
    private LocalDateTime timestamp;
    private String        template;
    public static EmailResponse success( String to, String subject, String traceId, String template )
    {
        return EmailResponse.builder().success( true ).to( to ).subject( subject )
                .message( AppConstants.EMAIL_SENT_SUCCESS_MESSAGE ).traceId( traceId ).timestamp( LocalDateTime.now() )
                .template( template ).build();
    }

    public static EmailResponse failure( String to, String subject, String errorMessage, String traceId )
    {
        return EmailResponse.builder().success( false ).to( to ).subject( subject )
                .message( AppConstants.ERROR_EMAIL_SENDING_FAILED ).errorMessage( errorMessage ).traceId( traceId )
                .timestamp( LocalDateTime.now() ).build();
    }
}
