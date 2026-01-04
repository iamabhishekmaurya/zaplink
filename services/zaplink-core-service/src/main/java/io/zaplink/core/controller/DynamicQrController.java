package io.zaplink.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.service.dynamicqr.DynamicQrService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Validated @RestController @RequiredArgsConstructor @RequestMapping("${api.base-path}/dyqr")
public class DynamicQrController
{
    private final DynamicQrService dynamicQrService;
    @PostMapping
    public ResponseEntity<DynamicQrResponse> createDynamicQr( @Valid @RequestBody CreateDynamicQrRequest request,
                                                              @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.createDynamicQr( request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping("/{qrKey}/destination")
    public ResponseEntity<DynamicQrResponse> updateDestination( @PathVariable("qrKey") String qrKey,
                                                                @Valid @RequestBody UpdateDestinationRequest request,
                                                                @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        DynamicQrResponse response = dynamicQrService.updateDestination( qrKey, request, userEmail );
        return ResponseEntity.ok( response );
    }

    @PutMapping("/{qrKey}/status")
    public ResponseEntity<Void> toggleQrStatus( @PathVariable("qrKey") String qrKey,
                                                @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        dynamicQrService.toggleQrStatus( qrKey, userEmail );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{qrKey}")
    public ResponseEntity<Void> deleteDynamicQr( @PathVariable("qrKey") String qrKey,
                                                 @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        dynamicQrService.deleteDynamicQr( qrKey, userEmail );
        return ResponseEntity.ok().build();
    }
}
