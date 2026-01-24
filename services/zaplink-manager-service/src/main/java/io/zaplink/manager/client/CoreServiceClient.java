package io.zaplink.manager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.zaplink.manager.dto.request.qr.QRConfig;

@FeignClient(name = "zaplink-core-service") // Uses Eureka by default
public interface CoreServiceClient
{
    @PostMapping(value = "${api.base-path}/qr/styled", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    ResponseEntity<byte[]> generateStyledQr( @RequestBody QRConfig config );
}
