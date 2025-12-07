package io.zaplink.notification.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface RegistreationEmailHandler
{
    Mono<ServerResponse> sendVerificationEmail( ServerRequest request );
}
