package io.zaplink.core.service.grpc;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import io.grpc.stub.StreamObserver;
import io.zaplink.core.entity.UrlMappingEntity;
import io.zaplink.core.grpc.CoreServiceGrpc;
import io.zaplink.core.grpc.CoreServiceProto;
import io.zaplink.core.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService @Slf4j @RequiredArgsConstructor
public class CoreGrpcService
        extends
        CoreServiceGrpc.CoreServiceImplBase
{
        private final UrlMappingRepository     urlMappingRepository;
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
        @Override
        public void getUrlByUser( CoreServiceProto.GetUrlByUserRequest request,
                                  StreamObserver<CoreServiceProto.GetUrlByUserResponse> responseObserver )
        {
                try
                {
                        log.info( "gRPC: Getting URLs for user: {}", request.getUserEmail() );
                        // Get all URLs for user and sort manually since repository doesn't have the method
                        List<UrlMappingEntity> allUrls = urlMappingRepository.findAll();
                        List<UrlMappingEntity> userUrls = allUrls.stream()
                                        .filter( url -> request.getUserEmail().equals( url.getUserEmail() ) )
                                        .sorted( ( a, b ) -> b.getCreatedAt().compareTo( a.getCreatedAt() ) )
                                        .skip( request.getPage() * request.getSize() ).limit( request.getSize() )
                                        .collect( Collectors.toList() );
                        List<CoreServiceProto.UrlData> urlDataList = userUrls.stream().map( this::mapToUrlData )
                                        .collect( Collectors.toList() );
                        CoreServiceProto.GetUrlByUserResponse response = CoreServiceProto.GetUrlByUserResponse
                                        .newBuilder().addAllUrls( urlDataList ).setTotalCount( userUrls.size() )
                                        .build();
                        responseObserver.onNext( response );
                        responseObserver.onCompleted();
                        log.info( "gRPC: Retrieved {} URLs for user: {}", userUrls.size(), request.getUserEmail() );
                }
                catch ( Exception e )
                {
                        log.error( "gRPC: Error getting URLs for user: {}", request.getUserEmail(), e );
                        responseObserver.onError( e );
                }
        }

        @Override
        public void getUrlById( CoreServiceProto.GetUrlByIdRequest request,
                                StreamObserver<CoreServiceProto.GetUrlByIdResponse> responseObserver )
        {
                try
                {
                        log.info( "gRPC: Getting URL by ID: {}", request.getUrlId() );
                        UrlMappingEntity url = urlMappingRepository.findById( Long.parseLong( request.getUrlId() ) )
                                        .orElseThrow( () -> new RuntimeException( "URL not found: "
                                                        + request.getUrlId() ) );
                        CoreServiceProto.GetUrlByIdResponse response = CoreServiceProto.GetUrlByIdResponse.newBuilder()
                                        .setUrl( mapToUrlData( url ) ).build();
                        responseObserver.onNext( response );
                        responseObserver.onCompleted();
                        log.info( "gRPC: Retrieved URL: {}", url.getShortUrl() );
                }
                catch ( Exception e )
                {
                        log.error( "gRPC: Error getting URL by ID: {}", request.getUrlId(), e );
                        responseObserver.onError( e );
                }
        }

        @Override
        public void getUrlsByStatus( CoreServiceProto.GetUrlsByStatusRequest request,
                                     StreamObserver<CoreServiceProto.GetUrlsByStatusResponse> responseObserver )
        {
                try
                {
                        log.info( "gRPC: Getting URLs by status: {} for user: {}", request.getStatus(),
                                  request.getUserEmail() );
                        // Get URLs by status and filter by user email
                        List<UrlMappingEntity> urlsByStatus = urlMappingRepository.findByStatus( request.getStatus() );
                        List<UrlMappingEntity> userUrls = urlsByStatus.stream()
                                        .filter( url -> request.getUserEmail().equals( url.getUserEmail() ) )
                                        .collect( Collectors.toList() );
                        List<CoreServiceProto.UrlData> urlDataList = userUrls.stream().map( this::mapToUrlData )
                                        .collect( Collectors.toList() );
                        CoreServiceProto.GetUrlsByStatusResponse response = CoreServiceProto.GetUrlsByStatusResponse
                                        .newBuilder().addAllUrls( urlDataList ).build();
                        responseObserver.onNext( response );
                        responseObserver.onCompleted();
                        log.info( "gRPC: Retrieved {} URLs with status: {} for user: {}", userUrls.size(),
                                  request.getStatus(), request.getUserEmail() );
                }
                catch ( Exception e )
                {
                        log.error( "gRPC: Error getting URLs by status: {} for user: {}", request.getStatus(),
                                   request.getUserEmail(), e );
                        responseObserver.onError( e );
                }
        }

        private CoreServiceProto.UrlData mapToUrlData( UrlMappingEntity entity )
        {
                return CoreServiceProto.UrlData.newBuilder().setId( entity.getId().toString() )
                                .setShortUrl( entity.getShortUrl() ).setOriginalUrl( entity.getOriginalUrl() )
                                .setUserEmail( entity.getUserEmail() ).setStatus( entity.getStatus().name() )
                                .setClickCount( entity.getClickCount() )
                                .setCreatedAt( entity.getCreatedAt().format( FORMATTER ) )
                                .setUpdatedAt( entity.getCreatedAt().format( FORMATTER ) ) // Using createdAt as updatedAt since entity doesn't have updatedAt
                                .setTitle( entity.getTitle() != null ? entity.getTitle() : "" ).setDescription( "" ) // Entity doesn't have description field
                                .setQrCodeUrl( "" ) // Entity doesn't have qrCodeUrl field
                                .build();
        }
}
