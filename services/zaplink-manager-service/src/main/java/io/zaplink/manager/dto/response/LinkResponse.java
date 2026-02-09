package io.zaplink.manager.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.common.enums.UrlStatusEnum;
import io.zaplink.manager.dto.RedirectRuleDto;

public record LinkResponse( Long id,
                            @JsonProperty("short_url_key") String shortUrlKey,
                            @JsonProperty("original_url") String originalUrl,
                            @JsonProperty("short_url") String shortUrl,
                            @JsonProperty("created_at") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC") LocalDateTime createdAt,
                            @JsonProperty("click_count") Long clickCount,
                            UrlStatusEnum status,
                            List<RedirectRuleDto> rules,
                            List<String> tags )
{
}
