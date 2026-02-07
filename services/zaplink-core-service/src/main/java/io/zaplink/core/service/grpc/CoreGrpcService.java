package io.zaplink.core.service.grpc;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.grpc.stub.StreamObserver;
import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.entity.DynamicQrCodeEntity;
import io.zaplink.core.entity.UrlMappingEntity;
import io.zaplink.core.grpc.CoreServiceGrpc;
import io.zaplink.core.grpc.CoreServiceProto;
import io.zaplink.core.repository.DynamicQrCodeRepository;
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
        private final DynamicQrCodeRepository  dynamicQrCodeRepository;
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
        @Override
        public void getUrlByUser( CoreServiceProto.GetUrlByUserRequest request,
                                  StreamObserver<CoreServiceProto.GetUrlByUserResponse> responseObserver )
        {
                try
                {
                        log.info( LogConstants.GRPC_GETTING_URLS_FOR_USER, request.getUserEmail() );
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
                        log.info( LogConstants.GRPC_RETRIEVED_URLS_FOR_USER, userUrls.size(), request.getUserEmail() );
                }
                catch ( Exception e )
                {
                        log.error( LogConstants.GRPC_ERROR_GETTING_URLS_FOR_USER, request.getUserEmail(), e );
                        responseObserver.onError( e );
                }
        }

        @Override
        public void getUrlById( CoreServiceProto.GetUrlByIdRequest request,
                                StreamObserver<CoreServiceProto.GetUrlByIdResponse> responseObserver )
        {
                try
                {
                        log.info( LogConstants.GRPC_GETTING_URL_BY_ID, request.getUrlId() );
                        UrlMappingEntity url = urlMappingRepository.findById( Long.parseLong( request.getUrlId() ) )
                                        .orElseThrow( () -> new RuntimeException( String
                                                        .format( ErrorConstant.ERROR_URL_NOT_FOUND,
                                                                 request.getUrlId() ) ) );
                        CoreServiceProto.GetUrlByIdResponse response = CoreServiceProto.GetUrlByIdResponse.newBuilder()
                                        .setUrl( mapToUrlData( url ) ).build();
                        responseObserver.onNext( response );
                        responseObserver.onCompleted();
                        log.info( LogConstants.GRPC_RETRIEVED_URL, url.getShortUrl() );
                }
                catch ( Exception e )
                {
                        log.error( LogConstants.GRPC_ERROR_GETTING_URL_BY_ID, request.getUrlId(), e );
                        responseObserver.onError( e );
                }
        }

        @Override
        public void getUrlsByStatus( CoreServiceProto.GetUrlsByStatusRequest request,
                                     StreamObserver<CoreServiceProto.GetUrlsByStatusResponse> responseObserver )
        {
                try
                {
                        log.info( LogConstants.GRPC_GETTING_URLS_BY_STATUS, request.getStatus(),
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
                        log.info( LogConstants.GRPC_RETRIEVED_URLS_BY_STATUS, userUrls.size(), request.getStatus(),
                                  request.getUserEmail() );
                }
                catch ( Exception e )
                {
                        log.error( LogConstants.GRPC_ERROR_GETTING_URLS_BY_STATUS, request.getStatus(),
                                   request.getUserEmail(), e );
                        responseObserver.onError( e );
                }
        }
        // ========== Dynamic QR gRPC Methods ==========

        @Override
        public void getDynamicQrsByUser( CoreServiceProto.GetDynamicQrsRequest request,
                                         StreamObserver<CoreServiceProto.GetDynamicQrsResponse> responseObserver )
        {
                try
                {
                        log.info( "gRPC: Getting dynamic QRs for user: {}", request.getUserEmail() );
                        Pageable pageable = PageRequest.of( request.getPage(), request.getSize(),
                                                            Sort.by( Sort.Direction.DESC, "createdAt" ) );
                        Page<DynamicQrCodeEntity> qrPage = dynamicQrCodeRepository
                                        .findByUserEmail( request.getUserEmail(), pageable );
                        List<CoreServiceProto.DynamicQrData> qrDataList = qrPage.getContent().stream()
                                        .map( this::mapToDynamicQrData ).collect( Collectors.toList() );
                        CoreServiceProto.GetDynamicQrsResponse response = CoreServiceProto.GetDynamicQrsResponse
                                        .newBuilder().addAllQrs( qrDataList )
                                        .setTotalCount( (int) qrPage.getTotalElements() )
                                        .setTotalPages( qrPage.getTotalPages() ).build();
                        responseObserver.onNext( response );
                        responseObserver.onCompleted();
                        log.info( "gRPC: Retrieved {} QRs for user: {}", qrDataList.size(), request.getUserEmail() );
                }
                catch ( Exception e )
                {
                        log.error( "gRPC: Error getting dynamic QRs for user: {}", request.getUserEmail(), e );
                        responseObserver.onError( e );
                }
        }

        @Override
        public void getDynamicQrByKey( CoreServiceProto.GetDynamicQrByKeyRequest request,
                                       StreamObserver<CoreServiceProto.DynamicQrData> responseObserver )
        {
                try
                {
                        log.info( "gRPC: Getting dynamic QR by key: {}", request.getQrKey() );
                        DynamicQrCodeEntity entity = dynamicQrCodeRepository.findByQrKey( request.getQrKey() )
                                        .orElseThrow( () -> new RuntimeException( "Dynamic QR not found: "
                                                        + request.getQrKey() ) );
                        // Verify ownership if userEmail provided
                        if ( request.getUserEmail() != null && !request.getUserEmail().isEmpty()
                                        && !request.getUserEmail().equals( entity.getUserEmail() ) )
                        {
                                throw new RuntimeException( "Access denied to QR: " + request.getQrKey() );
                        }
                        responseObserver.onNext( mapToDynamicQrData( entity ) );
                        responseObserver.onCompleted();
                        log.info( "gRPC: Retrieved QR: {}", request.getQrKey() );
                }
                catch ( Exception e )
                {
                        log.error( "gRPC: Error getting dynamic QR by key: {}", request.getQrKey(), e );
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

        private CoreServiceProto.DynamicQrData mapToDynamicQrData( DynamicQrCodeEntity entity )
        {
                CoreServiceProto.DynamicQrData.Builder builder = CoreServiceProto.DynamicQrData.newBuilder()
                                .setId( entity.getId() ).setQrKey( entity.getQrKey() )
                                .setQrName( entity.getQrName() != null ? entity.getQrName() : "" )
                                .setCurrentDestinationUrl( entity.getCurrentDestinationUrl() )
                                .setQrImageUrl( "http://localhost:8090/api/rd/qr/" + entity.getQrKey() + "/image" )
                                .setRedirectUrl( "http://localhost:8090/s/" + entity.getQrKey() )
                                .setCampaignId( entity.getCampaignId() != null ? entity.getCampaignId() : "" )
                                .setUserEmail( entity.getUserEmail() != null ? entity.getUserEmail() : "" )
                                .setIsActive( entity.getIsActive() != null ? entity.getIsActive() : true )
                                .setTotalScans( entity.getTotalScans() != null ? entity.getTotalScans() : 0L )
                                .setCreatedAt( entity.getCreatedAt() != null ? entity.getCreatedAt().format( FORMATTER )
                                                                             : "" )
                                .setUpdatedAt( entity.getUpdatedAt() != null ? entity.getUpdatedAt().format( FORMATTER )
                                                                             : "" )
                                .setLastScanned( entity.getLastScanned() != null
                                                                                 ? entity.getLastScanned()
                                                                                                 .format( FORMATTER )
                                                                                 : "" )
                                .setQrConfig( entity.getQrConfig() != null ? entity.getQrConfig() : "" )
                                .setAllowedDomains( entity.getAllowedDomains() != null ? entity.getAllowedDomains()
                                                                                       : "" )
                                .setPassword( entity.getPassword() != null ? entity.getPassword() : "" )
                                .setScanLimit( entity.getScanLimit() != null ? entity.getScanLimit() : 0 )
                                .setExpirationDate( entity.getExpirationDate() != null ? entity.getExpirationDate()
                                                .format( FORMATTER ) : "" )
                                .setTrackAnalytics( entity.getTrackAnalytics() != null ? entity.getTrackAnalytics()
                                                                                       : true );
                return builder.build();
        }
}
