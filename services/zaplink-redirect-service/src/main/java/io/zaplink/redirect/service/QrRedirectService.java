package io.zaplink.redirect.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.redirect.dto.event.QrScanEvent;
import io.zaplink.redirect.entity.DynamicQrCodeEntity;
import io.zaplink.redirect.repository.DynamicQrCodeRepository;
import io.zaplink.redirect.repository.RedirectRuleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for handling QR code redirects with advanced validation.
 * Supports expiration, scan limits, password protection, and domain restrictions.
 */
@Service @Slf4j @RequiredArgsConstructor
public class QrRedirectService
{
    private final DynamicQrCodeRepository dynamicQrCodeRepository;
    private final RedirectRuleRepository  redirectRuleRepository;
    private final GeoIpService            geoIpService;
    private final KafkaEventPublisher     kafkaEventPublisher;
    private final ObjectMapper            objectMapper;
    private final RuleEngine              ruleEngine;
    @Value("${redirect.error.base-url:https://zaplink.app/error}")
    private String                        errorBaseUrl;
    @Value("${redirect.password-protect.base-url:https://zaplink.app/password-protect}")
    private String                        passwordProtectBaseUrl;
    /**
     * Result of QR redirect resolution using sealed interface (Java 21).
     */
    public sealed interface QrRedirectResult
        permits
        QrRedirectResult.Success,
        QrRedirectResult.NotFound,
        QrRedirectResult.Inactive,
        QrRedirectResult.Expired,
        QrRedirectResult.LimitReached,
        QrRedirectResult.Forbidden,
        QrRedirectResult.PasswordRequired
    {
        record Success( String destinationUrl )
            implements
            QrRedirectResult
        {
        }
        record NotFound()
            implements
            QrRedirectResult
        {
        }
        record Inactive()
            implements
            QrRedirectResult
        {
        }
        record Expired()
            implements
            QrRedirectResult
        {
        }
        record LimitReached()
            implements
            QrRedirectResult
        {
        }
        record Forbidden()
            implements
            QrRedirectResult
        {
        }
        record PasswordRequired( String passwordUrl )
            implements
            QrRedirectResult
        {
        }
    }
    /**
     * Resolve QR redirect with full validation and analytics.
     *
     * @param qrKey   the QR key
     * @param request the HTTP request for validation and analytics
     * @return QrRedirectResult indicating success or failure reason
     */
    public QrRedirectResult resolveAndTrack( String qrKey, HttpServletRequest request )
    {
        log.debug( "Resolving QR redirect for key: {}", qrKey );
        // 1. Find QR entity
        Optional<DynamicQrCodeEntity> entityOpt = dynamicQrCodeRepository.findByQrKey( qrKey );
        if ( entityOpt.isEmpty() )
        {
            log.warn( "QR code not found for key: {}", qrKey );
            return new QrRedirectResult.NotFound();
        }
        DynamicQrCodeEntity entity = entityOpt.get();
        // 2. Check if active
        if ( !Boolean.TRUE.equals( entity.getIsActive() ) )
        {
            log.info( "QR code inactive: {}", qrKey );
            return new QrRedirectResult.Inactive();
        }
        // 3. Check expiration
        if ( entity.getExpirationDate() != null && entity.getExpirationDate().isBefore( LocalDateTime.now() ) )
        {
            log.info( "QR code expired: {}", qrKey );
            return new QrRedirectResult.Expired();
        }
        // 4. Check scan limit
        if ( entity.getScanLimit() != null && entity.getScanLimit() > 0
                && entity.getTotalScans() >= entity.getScanLimit() )
        {
            log.info( "QR code scan limit reached: {}", qrKey );
            return new QrRedirectResult.LimitReached();
        }
        // 5. Check domain restriction
        if ( entity.getAllowedDomains() != null && !entity.getAllowedDomains().isEmpty() )
        {
            String referer = request.getHeader( "Referer" );
            if ( !isDomainAllowed( referer, entity.getAllowedDomains() ) )
            {
                log.warn( "Access denied from referer: {}", referer );
                return new QrRedirectResult.Forbidden();
            }
        }
        // 6. Check password protection
        if ( entity.getPassword() != null && !entity.getPassword().isEmpty() )
        {
            String accessToken = request.getParameter( "access_token" );
            if ( accessToken == null )
            {
                log.info( "Password required for QR: {}", qrKey );
                return new QrRedirectResult.PasswordRequired( passwordProtectBaseUrl + "/" + qrKey );
            }
            // In production, verify accessToken against hashed password/session
        }
        // --- SMART ROUTING START ---
        // Fetch Rules (Lazy load or fetch from Repo)
        // For QR, we fetch rules directly from repo for now as we don't have them in Entity yet
        String finalDestination = entity.getCurrentDestinationUrl();
        var rules = redirectRuleRepository.findByDynamicQrCodeIdOrderByPriorityDesc( entity.getId() );
        if ( !rules.isEmpty() )
        {
            String ip = RequestUtils.getClientIpAddress( request );
            String ua = RequestUtils.getUserAgent( request );
            String country = request.getHeader( "CF-IPCountry" );
            if ( country == null || country.isEmpty() )
            {
                country = geoIpService.resolveLocation( ip ).get( "country" );
            }
            var context = RuleEngine.RoutingContext.builder().deviceType( RequestUtils.extractDeviceType( ua ) )
                    .os( RequestUtils.extractOS( ua ) ).country( country ).build();
            Optional<String> smartDest = ruleEngine.evaluate( rules, context );
            if ( smartDest.isPresent() )
            {
                finalDestination = smartDest.get();
            }
        }
        // --- SMART ROUTING END ---
        // 7. Publish analytics event (if tracking enabled)
        if ( Boolean.TRUE.equals( entity.getTrackAnalytics() ) )
        {
            publishScanEvent( qrKey, request );
        }
        return new QrRedirectResult.Success( finalDestination );
    }

    /**
     * Check if domain is allowed.
     */
    private boolean isDomainAllowed( String referer, String allowedDomainsJson )
    {
        if ( referer == null || referer.isEmpty() )
        {
            return false; // Strict: require referer if domains are restricted
        }
        try
        {
            List<String> domains = objectMapper.readValue( allowedDomainsJson, new TypeReference<List<String>>()
            {} );
            return domains.stream().anyMatch( referer::contains );
        }
        catch ( Exception e )
        {
            // Fallback: simple string contains check
            return allowedDomainsJson.contains( referer );
        }
    }

    /**
     * Publish QR scan event asynchronously to Kafka.
     */
    @Async
    private void publishScanEvent( String qrKey, HttpServletRequest request )
    {
        try
        {
            String ipAddress = RequestUtils.getClientIpAddress( request );
            String userAgent = RequestUtils.getUserAgent( request );
            String referrer = RequestUtils.getReferrer( request );
            // Resolve location
            Map<String, String> location = geoIpService.resolveLocation( ipAddress );
            QrScanEvent event = QrScanEvent.of( qrKey, ipAddress, userAgent, referrer, location.get( "country" ),
                                                location.get( "city" ), RequestUtils.extractDeviceType( userAgent ),
                                                RequestUtils.extractBrowser( userAgent ),
                                                UUID.randomUUID().toString() );
            kafkaEventPublisher.publishQrScanEvent( event );
        }
        catch ( Exception e )
        {
            log.error( "Error publishing scan event for key: {}", qrKey, e );
            // Don't fail redirect on analytics error
        }
    }

    /**
     * Get error redirect URL for a given reason.
     */
    public String getErrorUrl( String reason )
    {
        return errorBaseUrl + "?reason=" + reason;
    }
}
