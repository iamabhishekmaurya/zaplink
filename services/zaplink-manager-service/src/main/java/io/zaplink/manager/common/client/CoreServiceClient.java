package io.zaplink.manager.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.zaplink.manager.dto.request.qr.QRConfig;

@FeignClient(name = "zaplink-core-service", url = "${zaplink.services.core.url:http://localhost:8081}") // Direct URL fallback, no /core prefix
public interface CoreServiceClient
{
    @PostMapping(value = "/qr/styled", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE, headers = "X-API-Version=1")
    ResponseEntity<byte[]> generateStyledQr( @RequestBody QRConfig config );
}
