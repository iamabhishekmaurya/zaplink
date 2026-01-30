package io.zaplink.scheduler.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "zaplink-social-service", url = "${application.social-service.url:http://localhost:8088}")
public interface SocialClient
{
    @PostMapping("/social/publish")
    void publishPost( @RequestBody PublishRequest request );
    record PublishRequest( UUID accountId, String caption, String mediaUrl )
    {
    }
}
