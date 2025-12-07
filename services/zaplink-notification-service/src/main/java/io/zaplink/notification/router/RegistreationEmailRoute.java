package io.zaplink.notification.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.zaplink.notification.common.constants.ApiEndpointConstants;
import io.zaplink.notification.handler.RegistreationEmailHandler;
import lombok.AllArgsConstructor;

@Configuration 
@AllArgsConstructor
public class RegistreationEmailRoute
{
    private final RegistreationEmailHandler regEmailHandler;
    @Bean
    RouterFunction<ServerResponse> routeConfig()
    {
        return RouterFunctions.route( post( ApiEndpointConstants.VERIFICATION_EMAIL_ENDPOINT ),
                                      regEmailHandler::sendVerificationEmail );
    }

    RequestPredicate post( String path )
    {
        return RequestPredicates.POST( path );
    }
}
