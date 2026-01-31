package io.zaplink.manager.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.common.enums.UrlStatusEnum;
import io.zaplink.manager.dto.RedirectRuleDto;

public record LinkResponse( @JsonProperty("id") Long id,
                            @JsonProperty("short_url_key") String shortUrlKey,
                            @JsonProperty("original_url") String originalUrl,
                            @JsonProperty("short_url") String shortUrl,
                            @JsonProperty("created_at") LocalDateTime createdAt,
                            @JsonProperty("click_count") Long clickCount,
                            @JsonProperty("status") UrlStatusEnum status,
                            @JsonProperty("rules") List<RedirectRuleDto> rules,
                            @JsonProperty("tags") List<String> tags )
{
}
