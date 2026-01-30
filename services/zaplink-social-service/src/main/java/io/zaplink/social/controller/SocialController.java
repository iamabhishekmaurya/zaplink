package io.zaplink.social.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.social.common.constants.ApiConstants;
import io.zaplink.social.common.constants.LogMessages;
import io.zaplink.social.dto.record.PublishRequest;
import io.zaplink.social.entity.SocialAccount;
import io.zaplink.social.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for the Social Service.
 * <p>
 * Provides endpoints for managing social account connections and triggering content publication.
 * Designed to be consumed by the Frontend (UI) and internal Microservices (Scheduler).
 * </p>
 * 
 * <p><b>Security Note:</b></p>
 * This controller assumes that the API Gateway performs JWT validation and injects the
 * 'X-User-Id' header for user identification.
 */
@RestController @RequestMapping(ApiConstants.BASE_URI) @RequiredArgsConstructor @Slf4j
public class SocialController
{
    private final SocialService socialService;
    /**
     * Endpoint to connect a new social account.
     * <p>
     * Invoked after the user completes the OAuth flow on the client side and receives an auth code.
     * </p>
     * 
     * <p><b>Flow:</b></p>
     * <ol>
     *     <li>Intercepts request with provider and auth code.</li>
     *     <li>Extracts User ID from secure header.</li>
     *     <li>Delegates processing to {@link SocialService}.</li>
     * </ol>
     *
     * @param provider The social platform (e.g., INSTAGRAM).
     * @param code     The short-lived authorization code.
     * @param userId   The authenticated user ID (from Gateway).
     * @return 200 OK with the created {@link SocialAccount} details.
     */
    @PostMapping(ApiConstants.CONNECT_URI)
    public ResponseEntity<SocialAccount> connectAccount( @RequestParam String provider,
                                                         @RequestParam String code,
                                                         @RequestHeader(ApiConstants.HEADER_USER_ID) UUID userId )
    {
        log.info( LogMessages.CONTROLLER_CONNECT_REQUEST, provider, userId );
        SocialAccount account = socialService.connectAccount( provider, code, userId );
        log.info( LogMessages.CONTROLLER_CONNECT_SUCCESS, account.getId() );
        return ResponseEntity.ok( account );
    }

    /**
     * Endpoint to publish a post immediately.
     * <p>
     * Invoked explicitly by the user or triggered by the {@code Scheduler Service}.
     * </p>
     *
     * <p><b>Flow:</b></p>
     * <ol>
     *     <li>Logs request receipt.</li>
     *     <li>Calls service layer to execute publish.</li>
     *     <li>Returns 200 OK immediately if successful (synchronous).</li>
     * </ol>
     * 
     * <p><b>TODO:</b></p>
     * Change this to return 202 ACCEPTED if the service logic becomes asynchronous in the future.
     *
     * @param request Payload containing account ID, caption, and media URL.
     * @return 200 OK.
     */
    @PostMapping(ApiConstants.PUBLISH_URI)
    public ResponseEntity<Void> publishPost( @RequestBody PublishRequest request )
    {
        log.info( LogMessages.CONTROLLER_PUBLISH_REQUEST, request.accountId() );
        socialService.publishPost( request.accountId(), request.caption(), request.mediaUrl() );
        log.info( LogMessages.CONTROLLER_PUBLISH_SUCCESS );
        return ResponseEntity.ok().build();
    }
}
