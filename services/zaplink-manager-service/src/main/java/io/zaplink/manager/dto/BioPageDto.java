package io.zaplink.manager.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BioPageDto( Long id,
                          String username,
                          @JsonProperty("owner_id") String ownerId,
                          @JsonProperty("theme_config") String themeConfig,
                          @JsonProperty("avatar_url") String avatarUrl,
                          @JsonProperty("bio_text") String bioText,
                          @JsonProperty("created_at") LocalDateTime createdAt,
                          @JsonProperty("updated_at") LocalDateTime updatedAt,
                          List<BioLinkDto> links )
{
}
