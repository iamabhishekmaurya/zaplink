package io.zaplink.core.service.dynamicqr.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.entity.DynamicQrCodeEntity;
import io.zaplink.core.repository.DynamicQrCodeRepository;
import io.zaplink.core.service.dynamicqr.DynamicQrService;
import io.zaplink.core.utility.SnowflakeShortUrlKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrServiceImpl
    implements
    DynamicQrService
{
    private final DynamicQrCodeRepository dynamicQrCodeRepository;
    private final ObjectMapper            objectMapper;
    @Override @Transactional
    public DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail )
    {
        try
        {
            String qrKey = SnowflakeShortUrlKeyUtil.generateShortKey();
            // Convert QR config to JSON string
            String qrConfigJson = objectMapper.writeValueAsString( request.getQrConfig() );
            DynamicQrCodeEntity entity = DynamicQrCodeEntity.builder().qrKey( qrKey ).qrName( request.getQrName() )
                    .currentDestinationUrl( request.getDestinationUrl() ).qrConfig( qrConfigJson )
                    .userEmail( userEmail ).campaignId( request.getCampaignId() ).isActive( true )
                    .createdAt( LocalDateTime.now() ).updatedAt( LocalDateTime.now() ).totalScans( 0L ).build();
            entity = dynamicQrCodeRepository.save( entity );
            log.info( "Created dynamic QR with key: {} for user: {}", qrKey, userEmail );
            return convertToResponse( entity );
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error converting QR config to JSON", e );
            throw new RuntimeException( "Failed to process QR configuration", e );
        }
    }

    @Override @Transactional
    public DynamicQrResponse updateDestination( String qrKey, UpdateDestinationRequest request, String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        entity.setCurrentDestinationUrl( request.getDestinationUrl() );
        entity.setUpdatedAt( LocalDateTime.now() );
        entity = dynamicQrCodeRepository.save( entity );
        log.info( "Updated destination for QR key: {} by user: {}", qrKey, userEmail );
        return convertToResponse( entity );
    }

    @Override @Transactional
    public void toggleQrStatus( String qrKey, String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        entity.setIsActive( !entity.getIsActive() );
        entity.setUpdatedAt( LocalDateTime.now() );
        dynamicQrCodeRepository.save( entity );
        log.info( "Toggled status for QR key: {} to {} by user: {}", qrKey, entity.getIsActive(), userEmail );
    }

    @Override @Transactional
    public void deleteDynamicQr( String qrKey, String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        dynamicQrCodeRepository.delete( entityOpt.get() );
        log.info( "Deleted dynamic QR with key: {} by user: {}", qrKey, userEmail );
    }

    private DynamicQrResponse convertToResponse( DynamicQrCodeEntity entity )
    {
        DynamicQrResponse response = new DynamicQrResponse();
        response.setId( entity.getId() );
        response.setQrKey( entity.getQrKey() );
        response.setQrName( entity.getQrName() );
        response.setCurrentDestinationUrl( entity.getCurrentDestinationUrl() );
        response.setQrImageUrl( "/api/v1/dynamic-qr/" + entity.getQrKey() + "/image" );
        response.setRedirectUrl( generateRedirectUrl( entity.getQrKey() ) );
        response.setCampaignId( entity.getCampaignId() );
        response.setUserEmail( entity.getUserEmail() );
        response.setIsActive( entity.getIsActive() );
        response.setTotalScans( entity.getTotalScans() );
        response.setCreatedAt( entity.getCreatedAt() );
        response.setUpdatedAt( entity.getUpdatedAt() );
        response.setLastScanned( entity.getLastScanned() );
        return response;
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "https://zaplink.app/r/" + qrKey;
    }
}
