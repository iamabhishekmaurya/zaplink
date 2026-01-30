package io.zaplink.scheduler.common.client;

import io.zaplink.scheduler.common.constants.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = ApiConstants.SOCIAL_SERVICE_NAME, url = ApiConstants.SOCIAL_SERVICE_URL)
public interface SocialClient
{
    @PostMapping(ApiConstants.SOCIAL_PUBLISH_URI)
    void publishPost( @RequestBody PublishRequest request );
    record PublishRequest( UUID accountId, String caption, String mediaUrl )
    {
    }
}
