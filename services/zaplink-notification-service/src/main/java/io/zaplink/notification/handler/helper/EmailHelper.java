package io.zaplink.notification.handler.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Year;

import org.slf4j.MDC;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import io.zaplink.notification.common.constants.AppConstants;
import io.zaplink.notification.common.constants.LogConstants;
import io.zaplink.notification.dto.response.EmailResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j @Service @RequiredArgsConstructor
public class EmailHelper
{
    private final JavaMailSender javaMailSender;
    public Mono<EmailResponse> sendEmail( String to, String subject, String body, String templateUrl )
    {
        log.info( LogConstants.LOG_SENDING_EMAIL, to );
        String traceId = MDC.get( AppConstants.TRACE_ID_MDC_KEY );
        // Load template asynchronously
        return Mono.fromCallable( () -> loadTemplate( templateUrl ) ).subscribeOn( Schedulers.boundedElastic() )
                .flatMap( template -> {
                    try
                    {
                        String emailContent = template.replace( AppConstants.VERIFICATION_CODE_PLACEHOLDER, body )
                                .replace( AppConstants.YEAR_PLACEHOLDER, String.valueOf( Year.now().getValue() ) );
                        // Create message asynchronously
                        return Mono.fromCallable( () -> {
                            MimeMessage message = javaMailSender.createMimeMessage();
                            MimeMessageHelper helper = new MimeMessageHelper( message, true, "UTF-8" );
                            helper.setTo( to );
                            helper.setSubject( subject );
                            helper.setText( emailContent, true );
                            return message;
                        } ).subscribeOn( Schedulers.boundedElastic() );
                    }
                    catch ( Exception ex )
                    {
                        return Mono.error( new RuntimeException( AppConstants.ERROR_FAILED_TO_PREPARE_EMAIL, ex ) );
                    }
                } ).flatMap( message ->
                // Send email asynchronously
                Mono.fromCallable( () -> {
                    javaMailSender.send( message );
                    log.info( LogConstants.LOG_EMAIL_SENT_SUCCESSFULLY, to );
                    return EmailResponse.success( to, subject, traceId, templateUrl );
                } ).subscribeOn( Schedulers.boundedElastic() ) ).onErrorResume( ex -> {
                    log.error( LogConstants.LOG_EMAIL_SENDING_FAILED, to );
                    return Mono.just( EmailResponse.failure( to, subject, ex.getMessage(), traceId ) );
                } );
    }
 
    private String loadTemplate( String path )
        throws IOException
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream( path ))
        {
            if ( inputStream == null )
            {
                throw new IOException( AppConstants.ERROR_TEMPLATE_NOT_FOUND + path );
            }
            return StreamUtils.copyToString( inputStream, StandardCharsets.UTF_8 );
        }
    }
}