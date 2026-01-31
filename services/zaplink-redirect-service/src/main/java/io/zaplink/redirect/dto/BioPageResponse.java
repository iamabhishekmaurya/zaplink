package io.zaplink.redirect.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BioPageResponse {
    
    private Long id;
    
    private String username;
    
    @JsonProperty("owner_id")
    private String ownerId;
    
    @JsonProperty("theme_config")
    private String themeConfig;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("bio_text")
    private String bioText;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    private List<BioLinkResponse> links;
}
