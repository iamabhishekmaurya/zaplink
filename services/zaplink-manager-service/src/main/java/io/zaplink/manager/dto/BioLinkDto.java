package io.zaplink.manager.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class BioLinkDto
{
    private Long          id;
    @JsonProperty("page_id")
    private Long          pageId;
    private String        title;
    private String        url;
    private String        type;
    @JsonProperty("is_active")
    private Boolean       isActive;
    @JsonProperty("sort_order")
    private Integer       sortOrder;
    private Double        price;
    private String        currency;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
