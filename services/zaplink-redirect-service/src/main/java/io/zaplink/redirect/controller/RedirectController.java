package io.zaplink.redirect.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.redirect.common.client.ManagerServiceClient;
import io.zaplink.redirect.common.constants.ControllerConstants;
import io.zaplink.redirect.common.constants.StatusConstants;
import io.zaplink.redirect.dto.BioPageResponse;
import io.zaplink.redirect.service.QrRedirectService;
import io.zaplink.redirect.service.QrRedirectService.QrRedirectResult;
import io.zaplink.redirect.service.UrlRedirectService;
import io.zaplink.redirect.service.UrlRedirectService.RedirectResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * High-performance redirect controller.
 * Handles URL and QR code redirects with minimal latency.
 * 
 * Uses Java 21 features:
 * - Virtual threads for high concurrency
 * - Pattern matching with switch expressions
 * - Sealed interfaces for result types
 */
@Slf4j @RestController @RequestMapping("/") @RequiredArgsConstructor @Tag(name = ControllerConstants.TAG_REDIRECT, description = ControllerConstants.TAG_REDIRECT_DESC)
public class RedirectController
{
    private final UrlRedirectService   urlRedirectService;
    private final QrRedirectService    qrRedirectService;
    private final ManagerServiceClient managerServiceClient;
    /**
     * Redirect short URL to original destination.
     * Path: /r/{urlKey}
     */
    @GetMapping("r/{urlKey}") @Operation(summary = ControllerConstants.REDIRECT_URL_SUMMARY, description = ControllerConstants.REDIRECT_URL_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_302_FOUND, description = ControllerConstants.RESPONSE_302_URL_REDIRECT),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_URL_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_410_GONE, description = ControllerConstants.RESPONSE_410_URL_INACTIVE) })
    public void redirectUrl( @Parameter(description = ControllerConstants.PARAM_URL_KEY) @PathVariable("urlKey") String urlKey,
                             HttpServletRequest request,
                             HttpServletResponse response )
        throws IOException
    {
        log.debug( "URL redirect request for key: {}", urlKey );
        RedirectResult result = urlRedirectService.resolveAndTrack( urlKey, request );
        // Pattern matching with switch expression (Java 21)
        switch ( result )
        {
            case RedirectResult.Success success -> {
                response.sendRedirect( success.destinationUrl() );
            }
            case RedirectResult.NotFound() -> {
                response.sendError( HttpStatus.NOT_FOUND.value(), "URL not found" );
            }
            case RedirectResult.Expired() -> {
                response.sendRedirect( qrRedirectService.getErrorUrl( "expired" ) );
            }
            case RedirectResult.Inactive() -> {
                response.sendError( HttpStatus.GONE.value(), "URL is no longer active" );
            }
        }
    }

    /**
     * Redirect QR code scan to destination.
     * Path: /s/{qrKey}
     * 
     * Handles advanced QR features:
     * - Expiration
     * - Scan limits
     * - Password protection
     * - Domain restrictions
     */
    @GetMapping("s/{qrKey}") @Operation(summary = ControllerConstants.REDIRECT_QR_SUMMARY, description = ControllerConstants.REDIRECT_QR_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_302_FOUND, description = ControllerConstants.RESPONSE_302_QR_REDIRECT),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_QR_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_410_GONE, description = ControllerConstants.RESPONSE_410_QR_INACTIVE),
      @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ControllerConstants.RESPONSE_403_QR_FORBIDDEN) })
    public void redirectQr( @Parameter(description = ControllerConstants.PARAM_QR_KEY) @PathVariable("qrKey") String qrKey,
                            HttpServletRequest request,
                            HttpServletResponse response )
        throws IOException
    {
        log.debug( "QR redirect request for key: {}", qrKey );
        QrRedirectResult result = qrRedirectService.resolveAndTrack( qrKey, request );
        // Pattern matching with switch expression (Java 21)
        switch ( result )
        {
            case QrRedirectResult.Success success -> {
                response.sendRedirect( success.destinationUrl() );
            }
            case QrRedirectResult.NotFound() -> {
                response.sendError( HttpStatus.NOT_FOUND.value(), "QR code not found" );
            }
            case QrRedirectResult.Inactive() -> {
                response.sendError( HttpStatus.GONE.value(), "QR code is no longer active" );
            }
            case QrRedirectResult.Expired() -> {
                response.sendRedirect( qrRedirectService.getErrorUrl( "expired" ) );
            }
            case QrRedirectResult.LimitReached() -> {
                response.sendRedirect( qrRedirectService.getErrorUrl( "limit_reached" ) );
            }
            case QrRedirectResult.Forbidden() -> {
                response.sendRedirect( qrRedirectService.getErrorUrl( "forbidden" ) );
            }
            case QrRedirectResult.PasswordRequired passwordRequired -> {
                response.sendRedirect( passwordRequired.passwordUrl() );
            }
        }
    }

    /**
     * Health check endpoint for load balancers.
     */
    @GetMapping("health") @Operation(summary = ControllerConstants.REDIRECT_HEALTH_SUMMARY, description = ControllerConstants.REDIRECT_HEALTH_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_HEALTH_OK) })
    public String health()
    {
        return "OK";
    }

    /**
     * Get BioPage by username.
     * Path: /b/{username}
     */
    @GetMapping("b/{username}") @Operation(summary = ControllerConstants.REDIRECT_BIO_PAGE_SUMMARY, description = ControllerConstants.REDIRECT_BIO_PAGE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_BIO_PAGE_RETRIEVED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_BIO_PAGE_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_500_INTERNAL_SERVER_ERROR, description = ControllerConstants.RESPONSE_500_INTERNAL_ERROR) })
    public ResponseEntity<BioPageResponse> getBioPage( @Parameter(description = ControllerConstants.PARAM_USERNAME) @PathVariable("username") String username )
    {
        log.debug( "Bio page request for username: {}", username );
        try
        {
            BioPageResponse bioPage = managerServiceClient.getBioPageByUsername( username );
            if ( bioPage != null )
            {
                return ResponseEntity.ok( bioPage );
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }
        catch ( Exception e )
        {
            log.error( "Error fetching bio page for username: {}", username, e );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).build();
        }
    }
}
