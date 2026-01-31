package io.zaplink.manager.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BioLinkDto( Long id,
                          @JsonProperty("page_id") Long pageId,
                          String title,
                          String url,
                          String type,
                          @JsonProperty("is_active") Boolean isActive,
                          @JsonProperty("sort_order") Integer sortOrder,
                          Double price,
                          String currency,
                          @JsonProperty("created_at") LocalDateTime createdAt,
                          @JsonProperty("updated_at") LocalDateTime updatedAt )
{
}
