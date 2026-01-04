package io.zaplink.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.service.redirect.QrRedirectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController @RequestMapping("/r") @RequiredArgsConstructor
public class RedirectController
{
    private final QrRedirectService qrRedirectService;
    @GetMapping("/{qrKey}")
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
        catch ( Exception e )
        {
            log.error( "Error handling redirect for QR key: {}", qrKey, e );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).build();
        }
    }
}
