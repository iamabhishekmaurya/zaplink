package io.zaplink.notification.handler.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.zaplink.notification.common.constants.AppConstants;
import io.zaplink.notification.common.constants.LogConstants;
import io.zaplink.notification.dto.request.EmailRequest;
import io.zaplink.notification.handler.RegistreationEmailHandler;
import io.zaplink.notification.handler.helper.EmailHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j 
@Service 
@AllArgsConstructor
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
            try
            {
                return emailHelper
                        .sendEmail( emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody(),
                                    AppConstants.REGISTRATION_VERIFICATION_TEMPLATE )
                        .then( Mono.just( AppConstants.EMAIL_SENT_SUCCESS_MESSAGE ) );
            }
            catch ( IOException e )
            {
                log.error( LogConstants.LOG_EMAIL_REQUEST_VALIDATION_FAILED, emailRequest.getTo(), e.getMessage() );
                return Mono.error( new RuntimeException( AppConstants.EMAIL_PROCESSING_FAILED_ERROR, e ) );
            }
        } ).flatMap( response -> ServerResponse.ok().body( Mono.just( response ), String.class ) )
                .onErrorResume( ex -> {
                    log.error( LogConstants.LOG_EMAIL_SENDING_FAILED, "unknown", ex.getMessage() );
                    Mono<String> errorResponse = Mono
                            .just( AppConstants.FAILED_TO_SEND_VERIFICATION_EMAIL_ERROR + ex.getMessage() );
                    return ServerResponse.badRequest().body( errorResponse, String.class );
                } );
    }
}