package io.zaplink.manager.common.client;

import org.springframework.stereotype.Component;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.zaplink.core.grpc.CoreServiceGrpc;
import io.zaplink.core.grpc.CoreServiceProto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 * gRPC Client for calling Core Service's Dynamic QR methods.
 */
@Component @Slf4j
public class CoreQrGrpcClient
{
    private ManagedChannel                          channel;
    private CoreServiceGrpc.CoreServiceBlockingStub blockingStub;
    @PostConstruct
    public void init()
    {
        // TODO: Make host/port configurable via application.yml
        String host = "localhost";
        int port = 50051;
        log.info( "Initializing gRPC client for Core Service at {}:{}", host, port );
        channel = ManagedChannelBuilder.forAddress( host, port ).usePlaintext().build();
        blockingStub = CoreServiceGrpc.newBlockingStub( channel );
    }

    @PreDestroy
    public void shutdown()
    {
        if ( channel != null && !channel.isShutdown() )
        {
            log.info( "Shutting down gRPC client for Core Service" );
            channel.shutdown();
        }
    }

    /**
     * Get paginated list of Dynamic QRs for a user.
     */
    public CoreServiceProto.GetDynamicQrsResponse getDynamicQrsByUser( String userEmail, int page, int size )
    {
        log.debug( "gRPC Client: Getting QRs for user: {}, page: {}, size: {}", userEmail, page, size );
        CoreServiceProto.GetDynamicQrsRequest request = CoreServiceProto.GetDynamicQrsRequest.newBuilder()
                .setUserEmail( userEmail != null ? userEmail : "" ).setPage( page ).setSize( size ).build();
        return blockingStub.getDynamicQrsByUser( request );
    }

    /**
     * Get a single Dynamic QR by its key.
     */
    public CoreServiceProto.DynamicQrData getDynamicQrByKey( String qrKey, String userEmail )
    {
        log.debug( "gRPC Client: Getting QR by key: {}", qrKey );
        CoreServiceProto.GetDynamicQrByKeyRequest request = CoreServiceProto.GetDynamicQrByKeyRequest.newBuilder()
                .setQrKey( qrKey ).setUserEmail( userEmail != null ? userEmail : "" ).build();
        return blockingStub.getDynamicQrByKey( request );
    }
}
