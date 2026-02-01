package io.zaplink.manager.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.zaplink.auth.grpc.AuthServiceGrpc;
import io.zaplink.core.grpc.CoreServiceGrpc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.core-service.host:localhost}")
    private String coreServiceHost;

    @Value("${grpc.core-service.port:50051}")
    private int coreServicePort;

    @Value("${grpc.auth-service.host:localhost}")
    private String authServiceHost;

    @Value("${grpc.auth-service.port:50052}")
    private int authServicePort;

    @Bean
    public ManagedChannel coreServiceChannel() {
        return ManagedChannelBuilder.forAddress(coreServiceHost, coreServicePort)
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel authServiceChannel() {
        return ManagedChannelBuilder.forAddress(authServiceHost, authServicePort)
                .usePlaintext()
                .build();
    }

    @Bean
    public CoreServiceGrpc.CoreServiceBlockingStub coreServiceBlockingStub(@Qualifier("coreServiceChannel") ManagedChannel coreServiceChannel) {
        return CoreServiceGrpc.newBlockingStub(coreServiceChannel);
    }

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub(@Qualifier("authServiceChannel") ManagedChannel authServiceChannel) {
        return AuthServiceGrpc.newBlockingStub(authServiceChannel);
    }
}
