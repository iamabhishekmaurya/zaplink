package io.zaplink.notification.handler.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Year;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import io.zaplink.notification.common.constants.AppConstants;
import io.zaplink.notification.common.constants.LogConstants;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j @Service @RequiredArgsConstructor
public class EmailHelper
{
    private final JavaMailSender javaMailSender;
    public Mono<Void> sendEmail( String to, String subject, String body, String templateUrl )
        throws IOException
    {
        log.info( LogConstants.LOG_SENDING_EMAIL, to );
        return Mono.just( loadTemplate( templateUrl ) ).flatMap( template -> {
            try
            {
                String emailContent = template.replace( AppConstants.VERIFICATION_CODE_PLACEHOLDER, body )
                        .replace( AppConstants.YEAR_PLACEHOLDER, String.valueOf( Year.now().getValue() ) );
                return Mono.fromCallable( () -> {
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper( message, true, "UTF-8" );
                    helper.setTo( to );
                    helper.setSubject( subject );
                    helper.setText( emailContent, true );
                    return message;
                } ).subscribeOn( Schedulers.boundedElastic() );
            }
            catch ( Exception e )
            {
                return Mono.error( new RuntimeException( AppConstants.FAILED_TO_PREPARE_EMAIL_ERROR, e ) );
            }
        } ).flatMap( message -> Mono.fromRunnable( () -> {
            try
            {
                javaMailSender.send( message );
                log.info( LogConstants.LOG_EMAIL_SENT_SUCCESSFULLY, to );
            }
            catch ( Exception e )
            {
                log.error( LogConstants.LOG_EMAIL_SENDING_FAILED, to );
                throw new RuntimeException( AppConstants.FAILED_TO_SEND_EMAIL_ERROR + e.getMessage(), e );
            }
        } ).subscribeOn( Schedulers.boundedElastic() ) ).then();
    }

    private String loadTemplate( String path )
        throws IOException
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream( path ))
        {
            if ( inputStream == null )
            {
                throw new IOException( AppConstants.TEMPLATE_NOT_FOUND_ERROR + path );
            }
            return StreamUtils.copyToString( inputStream, StandardCharsets.UTF_8 );
        }
    }
}