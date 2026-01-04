package io.zaplink.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.zaplink.manager.dto.request.AnalyticsEvent;
import io.zaplink.manager.service.redirect.QrRedirectService;
import io.zaplink.manager.service.url.UrlManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController @RequestMapping("/") @RequiredArgsConstructor
public class RedirectController
{
    private final QrRedirectService qrRedirectService;
    private final UrlManagerService urlManagerService;
    @GetMapping("s/{qrKey}")
    public ResponseEntity<Void> redirectToDestination( @PathVariable("qrKey") String qrKey,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response )
    {
        try
        {
            boolean success = qrRedirectService.handleQrRedirect( qrKey, request, response );
            if ( success )
            {
                // Response is already handled by the service (redirect sent)
                return null;
            }
            else
            {
                // QR not found or inactive, return 404
                return ResponseEntity.notFound().build();
            }
        }
        catch ( Exception ex )
        {
            log.error( "Error handling redirect for QR key: {}", qrKey, ex );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).build();
        }
    }

    @GetMapping("r/{urlKey}")
    public RedirectView redirect( @PathVariable("urlKey") String urlKey, HttpServletRequest request )
    {
        try
        {
            AnalyticsEvent analyticsEvent = AnalyticsEvent.builder().urlKey( urlKey )
                    .ipAddress( request.getRemoteAddr() ).userAgent( request.getHeader( "User-Agent" ) )
                    .referrer( request.getHeader( "Referer" ) ).timestamp( java.time.LocalDateTime.now() ).build();
            String longUrl = urlManagerService.getShortUrl( analyticsEvent );
            if ( longUrl != null )
            {
                return new RedirectView( longUrl );
            }
            else
            {
                // If not found, you might want to redirect to a 404 page or return 404
                // RedirectView default is 302. For not found, we might want a different behavior.
                // But for now, let's redirect to a default 404 page if null (or handle exception).
                // Assuming getShortUrl returns null if not found.
                // Ideally, getShortUrl should throw a specific exception if not found, 
                // which we can handle with an exception handler. 
                // For now, let's assume it returns a valid URL or throws.
                // If it returns null, we can redirect to a generic 404 page on the UI.
                // Let's assume zaplink frontend handles /404.
                return new RedirectView( "http://localhost:3000/404" );
            }
        }
        catch ( Exception e )
        {
            log.error( "Error redirecting urlKey: {}", urlKey, e );
            return new RedirectView( "http://localhost:3000/error" );
        }
    }
}
