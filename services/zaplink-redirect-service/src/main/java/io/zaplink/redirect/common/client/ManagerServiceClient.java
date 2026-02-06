package io.zaplink.redirect.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.zaplink.redirect.dto.BioPageResponse;
import io.zaplink.redirect.dto.BioLinkResponse;

@FeignClient(name = "zaplink-manager-service", url = "${zaplink.services.manager.url:http://localhost:8083}")
public interface ManagerServiceClient
{
    @GetMapping("/api/v1/bio-pages/username/{username}")
    BioPageResponse getBioPageByUsername( @PathVariable("username") String username );

    @GetMapping("/api/v1/bio-links/page/{pageId}/active")
    List<BioLinkResponse> getActiveBioLinksByPageId( @PathVariable("pageId") Long pageId );
}
