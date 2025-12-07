package io.zaplink.notification.handler.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.zaplink.notification.common.constants.AppConstants;
import io.zaplink.notification.common.constants.LogConstants;
import io.zaplink.notification.dto.request.EmailRequest;
import io.zaplink.notification.dto.response.EmailResponse;
import io.zaplink.notification.handler.RegistreationEmailHandler;
import io.zaplink.notification.handler.helper.EmailHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j @Service @AllArgsConstructor
public class RegistreationEmailHandlerImpl
    implements
    RegistreationEmailHandler
{
    private final EmailHelper emailHelper;
    @Override
    public Mono<ServerResponse> sendVerificationEmail( ServerRequest request )
    {
        return request.bodyToMono( EmailRequest.class ).flatMap( emailRequest -> {
            log.info( LogConstants.LOG_PROCESSING_REGISTRATION_EMAIL_REQUEST, emailRequest.getTo() );
            return emailHelper.sendEmail( emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody(),
                                          AppConstants.TEMPLATE_REGISTRATION_VERIFICATION );
        } ).flatMap( emailResponse -> {
            if ( emailResponse.isSuccess() )
            {
                return ServerResponse.ok().body( Mono.just( emailResponse ), EmailResponse.class );
            }
            else
            {
                return ServerResponse.badRequest().body( Mono.just( emailResponse ), EmailResponse.class );
            }
        } ).onErrorResume( ex -> {
            log.error( LogConstants.LOG_EMAIL_SENDING_FAILED, "unknown", ex.getMessage() );
            EmailResponse errorResponse = EmailResponse.failure( "unknown", "verification email", ex.getMessage(),
                                                                 "N/A" );
            return ServerResponse.badRequest().body( Mono.just( errorResponse ), EmailResponse.class );
        } );
    }
}