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
            log.info( "Creating dynamic QR code. Name: {}, User: {}", request.qrName(), userEmail );
            String qrKey = SnowflakeShortUrlKeyUtil.generateShortKey();
            // Convert QR config to JSON string
            String qrConfigJson = objectMapper.writeValueAsString( request.qrConfig() );
            LocalDateTime expirationDate = null;
            if ( request.expirationDays() != null && request.expirationDays() > 0 )
            {
                expirationDate = LocalDateTime.now().plusDays( request.expirationDays() );
            }
            String allowedDomainsJson = null;
            if ( request.allowedDomains() != null && !request.allowedDomains().isEmpty() )
            {
                // Convert CSV to JSON Array
                List<String> domains = Arrays.stream( request.allowedDomains().split( "," ) ).map( String::trim )
                        .filter( s -> !s.isEmpty() ).collect( Collectors.toList() );
                allowedDomainsJson = objectMapper.writeValueAsString( domains );
            }
            DynamicQrCodeEntity entity = DynamicQrCodeEntity.builder().qrKey( qrKey ).qrName( request.qrName() )
                    .currentDestinationUrl( request.destinationUrl() ).qrConfig( qrConfigJson ).userEmail( userEmail )
                    .campaignId( request.campaignId() ).isActive( true ).createdAt( LocalDateTime.now() )
                    .updatedAt( LocalDateTime.now() ).totalScans( 0L ).expirationDate( expirationDate )
                    .password( request.password() ).scanLimit( request.scanLimit() )
                    .allowedDomains( allowedDomainsJson )
                    .trackAnalytics( request.trackAnalytics() != null ? request.trackAnalytics() : true ).build();
            entity = dynamicQrCodeRepository.save( entity );
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
        entity.setCurrentDestinationUrl( request.destinationUrl() );
        entity.setUpdatedAt( LocalDateTime.now() );
        entity = dynamicQrCodeRepository.save( entity );
        // Update Rules
        if ( request.rules() != null )
        {
            redirectRuleRepository.deleteByDynamicQrCodeId( entity.getId() );
            if ( !request.rules().isEmpty() )
            {
                final Long qrId = entity.getId();
                List<RedirectRuleEntity> ruleEntities = request.rules().stream()
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
        // Advanced Fields - parse JSON to objects
        Object qrConfig = null;
        Object allowedDomains = null;
        try
        {
            if ( entity.getQrConfig() != null )
            {
                qrConfig = objectMapper.readTree( entity.getQrConfig() );
            }
            if ( entity.getAllowedDomains() != null )
            {
                allowedDomains = objectMapper.readTree( entity.getAllowedDomains() );
            }
        }
        catch ( JsonProcessingException e )
        {
            log.error( "Error parsing JSON fields for response", e );
            // Fallback to null or ignore, but logging is important
        }
        return new DynamicQrResponse( entity.getId(),
                                      entity.getQrKey(),
                                      entity.getQrName(),
                                      entity.getCurrentDestinationUrl(),
                                      "/api/v1/dynamic-qr/" + entity.getQrKey() + "/image",
                                      generateRedirectUrl( entity.getQrKey() ),
                                      entity.getCampaignId(),
                                      entity.getUserEmail(),
                                      entity.getIsActive(),
                                      entity.getTotalScans(),
                                      entity.getCreatedAt(),
                                      entity.getUpdatedAt(),
                                      entity.getLastScanned(),
                                      // Advanced Fields
                                      qrConfig,
                                      allowedDomains,
                                      entity.getPassword(),
                                      entity.getScanLimit(),
                                      entity.getExpirationDate(),
                                      entity.getTrackAnalytics() );
    }

    private String generateRedirectUrl( String qrKey )
    {
        // This should be configurable based on your domain
        return "https://zaplink.app/s/" + qrKey;
    }
}
