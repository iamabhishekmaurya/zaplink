package io.zaplink.manager.dto.bio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BioLinkResponse
{
    private Long          id;
    @JsonProperty("page_id")
    private Long          pageId;
    private String        title;
    private String        url;
    private String        type;
    @JsonProperty("is_active")
    private boolean       isActive;
    @JsonProperty("sort_order")
    private int           sortOrder;
    private BigDecimal    price;
    private String        currency;
    private String        metadata;
    @JsonProperty("schedule_from")
    private LocalDateTime scheduleFrom;
    @JsonProperty("schedule_to")
    private LocalDateTime scheduleTo;
    @JsonProperty("icon_url")
    private String        iconUrl;
    @JsonProperty("thumbnail_url")
    private String        thumbnailUrl;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
