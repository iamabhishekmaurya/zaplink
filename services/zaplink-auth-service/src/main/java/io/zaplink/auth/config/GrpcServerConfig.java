package io.zaplink.auth.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.zaplink.auth.grpc.AuthServiceGrpc;
import io.zaplink.auth.service.grpc.AuthGrpcService;
import io.grpc.protobuf.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Configuration
public class GrpcServerConfig {

    @Value("${grpc.auth-service.port:50052}")
    private int grpcPort;

    private Server grpcServer;

    @Bean
    public Server grpcServer(AuthGrpcService authGrpcService, HealthStatusManager healthStatusManager) throws IOException {
        grpcServer = ServerBuilder.forPort(grpcPort)
                .addService(authGrpcService)
                .addService(healthStatusManager.getHealthService())
                .build()
                .start();
        
        log.info("gRPC Auth Server started on port: {}", grpcPort);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC Auth Server...");
            grpcServer.shutdown();
            try {
                if (!grpcServer.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                    grpcServer.shutdownNow();
                }
            } catch (InterruptedException e) {
                grpcServer.shutdownNow();
            }
            log.info("gRPC Auth Server stopped");
        }));
        
        return grpcServer;
    }

    @Bean
    public HealthStatusManager healthStatusManager() {
        return new HealthStatusManager();
    }

    @PreDestroy
    public void cleanup() {
        if (grpcServer != null) {
            grpcServer.shutdown();
            try {
                if (!grpcServer.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    grpcServer.shutdownNow();
                }
            } catch (InterruptedException e) {
                grpcServer.shutdownNow();
            }
        }
    }
}
