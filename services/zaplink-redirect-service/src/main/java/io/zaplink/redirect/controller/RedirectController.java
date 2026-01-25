package io.zaplink.redirect.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController @RequestMapping("/") @RequiredArgsConstructor @Slf4j
public class RedirectController
{
    private final UrlRedirectService urlRedirectService;
    private final QrRedirectService  qrRedirectService;
    /**
     * Redirect short URL to original destination.
     * Path: /r/{urlKey}
     */
    @GetMapping("r/{urlKey}")
    public void redirectUrl( @PathVariable("urlKey") String urlKey,
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
    @GetMapping("s/{qrKey}")
    public void redirectQr( @PathVariable("qrKey") String qrKey,
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
    @GetMapping("health")
    public String health()
    {
        return "OK";
    }
}
