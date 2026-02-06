package io.zaplink.social.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.social.common.constants.ApiConstants;
import io.zaplink.social.common.constants.ControllerConstants;
import io.zaplink.social.common.constants.LogMessages;
import io.zaplink.social.common.constants.StatusConstants;
import io.zaplink.social.dto.record.PublishRequest;
import io.zaplink.social.dto.response.SocialAccountResponse;
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
@RestController @RequestMapping(ApiConstants.BASE_URI) @RequiredArgsConstructor @Slf4j @Tag(name = ControllerConstants.TAG_SOCIAL, description = ControllerConstants.TAG_SOCIAL_DESC)
public class SocialController
{
    private final SocialService socialService;
    /**
     * Endpoint to connect a new social account.
     * <p>
     * Invoked after the user completes the OAuth flow on the client side and receives an auth code.
     * </p>
     */
    @PostMapping(ApiConstants.CONNECT_URI) @Operation(summary = ControllerConstants.SOCIAL_CONNECT_SUMMARY, description = ControllerConstants.SOCIAL_CONNECT_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_ACCOUNT_CONNECTED, content = @Content(schema = @Schema(implementation = SocialAccountResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_REQUEST) })
    public ResponseEntity<SocialAccountResponse> connectAccount( @Parameter(description = ControllerConstants.PARAM_PROVIDER) @RequestParam String provider,
                                                                 @Parameter(description = ControllerConstants.PARAM_AUTH_CODE) @RequestParam String code,
                                                                 @Parameter(description = ControllerConstants.PARAM_USER_ID, hidden = true) @RequestHeader(ApiConstants.HEADER_USER_ID) UUID userId )
    {
        log.info( LogMessages.CONTROLLER_CONNECT_REQUEST, provider, userId );
        SocialAccount account = socialService.connectAccount( provider, code, userId );
        log.info( LogMessages.CONTROLLER_CONNECT_SUCCESS, account.getId() );
        return ResponseEntity.ok( SocialAccountResponse.from( account ) );
    }

    /**
     * Endpoint to publish a post immediately.
     */
    @PostMapping(ApiConstants.PUBLISH_URI) @Operation(summary = ControllerConstants.SOCIAL_PUBLISH_SUMMARY, description = ControllerConstants.SOCIAL_PUBLISH_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_POST_PUBLISHED),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_PUBLISH_FAILED) })
    public ResponseEntity<Void> publishPost( @RequestBody PublishRequest request )
    {
        log.info( LogMessages.CONTROLLER_PUBLISH_REQUEST, request.accountId() );
        socialService.publishPost( request.accountId(), request.caption(), request.mediaUrl() );
        log.info( LogMessages.CONTROLLER_PUBLISH_SUCCESS );
        return ResponseEntity.ok().build();
    }
}
