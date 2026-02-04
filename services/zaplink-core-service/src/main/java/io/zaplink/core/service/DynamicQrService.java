package io.zaplink.core.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.entity.DynamicQrCodeEntity;
import io.zaplink.core.entity.RedirectRuleEntity;
import io.zaplink.core.repository.DynamicQrCodeRepository;
import io.zaplink.core.repository.RedirectRuleRepository;
import io.zaplink.core.utility.SnowflakeShortUrlKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class DynamicQrService
{
    private final DynamicQrCodeRepository dynamicQrCodeRepository;
    private final RedirectRuleRepository  redirectRuleRepository;
    private final ObjectMapper            objectMapper;
    @Transactional
    public DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail )
    {
        try
        {
            log.info( "Creating dynamic QR code. Name: {}, User: {}", request.getQrName(), userEmail );
            String qrKey = SnowflakeShortUrlKeyUtil.generateShortKey();
            // Convert QR config to JSON string
            String qrConfigJson = objectMapper.writeValueAsString( request.getQrConfig() );
            LocalDateTime expirationDate = null;
            if ( request.getExpirationDays() != null && request.getExpirationDays() > 0 )
            {
                expirationDate = LocalDateTime.now().plusDays( request.getExpirationDays() );
            }
            String allowedDomainsJson = null;
            if ( request.getAllowedDomains() != null && !request.getAllowedDomains().isEmpty() )
            {
                // Convert CSV to JSON Array
                List<String> domains = Arrays.stream( request.getAllowedDomains().split( "," ) ).map( String::trim )
                        .filter( s -> !s.isEmpty() ).collect( Collectors.toList() );
                allowedDomainsJson = objectMapper.writeValueAsString( domains );
            }
            DynamicQrCodeEntity entity = DynamicQrCodeEntity.builder()
            .qrKey( qrKey )
            .qrName( request.getQrName() )
            .currentDestinationUrl( request.getDestinationUrl() )
            .qrConfig( qrConfigJson )
            .userEmail( userEmail )
            .campaignId( request.getCampaignId() )
            .isActive( true )
            .createdAt( LocalDateTime.now() )
            .updatedAt( LocalDateTime.now() )
            .totalScans( 0L )
            .expirationDate( expirationDate )
            .password( request.getPassword() )
            .scanLimit( request.getScanLimit() )
            .allowedDomains( allowedDomainsJson )
            .trackAnalytics( request.getTrackAnalytics() != null ? request.getTrackAnalytics() : true ).build();
            entity = dynamicQrCodeRepository.save( entity );
            if ( request.getRules() != null && !request.getRules().isEmpty() )
            {
                final Long qrId = entity.getId();
                List<RedirectRuleEntity> ruleEntities = request.getRules().stream()
                        .map( r -> RedirectRuleEntity.builder()
                        .dynamicQrCodeId( qrId )
                        .dimension( r.dimension() )
                        .value( r.value() )
                        .destinationUrl( r.destinationUrl() )
                        .priority( r.priority() )
                        .createdAt( LocalDateTime.now() )
                        .build() )
                        .collect( Collectors.toList() );
                redirectRuleRepository.saveAll( ruleEntities );
            }
            log.info( "Created dynamic QR with key: {} for user: {}", qrKey, userEmail );
            return convertToResponse( entity );
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error converting QR config to JSON", e );
            throw new RuntimeException( "Failed to process QR configuration", e );
        }
    }

    @Transactional
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
        // Update Rules
        if ( request.getRules() != null )
        {
            redirectRuleRepository.deleteByDynamicQrCodeId( entity.getId() );
            if ( !request.getRules().isEmpty() )
            {
                final Long qrId = entity.getId();
                List<RedirectRuleEntity> ruleEntities = request.getRules().stream()
                        .map( r -> RedirectRuleEntity.builder().dynamicQrCodeId( qrId ).dimension( r.dimension() )
                                .value( r.value() ).destinationUrl( r.destinationUrl() ).priority( r.priority() )
                                .createdAt( LocalDateTime.now() ).build() )
                        .collect( Collectors.toList() );
                redirectRuleRepository.saveAll( ruleEntities );
            }
        }
        log.info( "Updated destination for QR key: {} by user: {}", qrKey, userEmail );
        return convertToResponse( entity );
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public DynamicQrResponse updateDynamicQr( String qrKey,
                                              io.zaplink.core.dto.request.dynamicqr.UpdateDynamicQrRequest request,
                                              String userEmail )
    {
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() || !entityOpt.get().getUserEmail().equals( userEmail ) )
        {
            throw new IllegalArgumentException( "Dynamic QR not found or access denied" );
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        try
        {
            // Update Basic Fields
            if ( request.qrName() != null )
                entity.setQrName( request.qrName() );
            if ( request.destinationUrl() != null )
                entity.setCurrentDestinationUrl( request.destinationUrl() );
            if ( request.qrConfig() != null )
                entity.setQrConfig( objectMapper.writeValueAsString( request.qrConfig() ) );
            if ( request.campaignId() != null )
                entity.setCampaignId( request.campaignId() );
            // Update Advanced Fields
            if ( request.expirationDate() != null )
                entity.setExpirationDate( request.expirationDate() );
            if ( request.password() != null )
                entity.setPassword( request.password() );
            if ( request.scanLimit() != null )
                entity.setScanLimit( request.scanLimit() );
            if ( request.trackAnalytics() != null )
                entity.setTrackAnalytics( request.trackAnalytics() );
            if ( request.allowedDomains() != null )
            {
                entity.setAllowedDomains( objectMapper.writeValueAsString( request.allowedDomains() ) );
            }
            entity.setUpdatedAt( LocalDateTime.now() );
            entity = dynamicQrCodeRepository.save( entity );
            // Update Rules
            redirectRuleRepository.deleteByDynamicQrCodeId( entity.getId() );
            redirectRuleRepository.flush(); // Critical for unique constraint
            if ( request.rules() != null && !request.rules().isEmpty() )
            {
                final Long qrId = entity.getId();
                List<RedirectRuleEntity> ruleEntities = request.rules().stream()
                        .map( r -> RedirectRuleEntity.builder().dynamicQrCodeId( qrId ).dimension( r.dimension() )
                                .value( r.value() ).destinationUrl( r.destinationUrl() ).priority( r.priority() )
                                .createdAt( LocalDateTime.now() ).build() )
                        .collect( Collectors.toList() );
                redirectRuleRepository.saveAll( ruleEntities );
            }
            log.info( "Updated dynamic QR with key: {} for user: {}", qrKey, userEmail );
            return convertToResponse( entity );
        }
        catch ( JsonProcessingException e )
        {
            throw new RuntimeException( "Failed to process QR configuration update", e );
        }
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
        // Advanced Fields
        try
        {
            if ( entity.getQrConfig() != null )
            {
                response.setQrConfig( objectMapper.readTree( entity.getQrConfig() ) );
            }
            if ( entity.getAllowedDomains() != null )
            {
                response.setAllowedDomains( objectMapper.readTree( entity.getAllowedDomains() ) );
            }
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error parsing JSON fields for response", e );
            // Fallback to null or ignore, but logging is important
        }
        response.setPassword( entity.getPassword() );
        response.setScanLimit( entity.getScanLimit() );
        response.setExpirationDate( entity.getExpirationDate() );
        response.setTrackAnalytics( entity.getTrackAnalytics() );
        return response;
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "https://zaplink.app/s/" + qrKey;
    }
}
