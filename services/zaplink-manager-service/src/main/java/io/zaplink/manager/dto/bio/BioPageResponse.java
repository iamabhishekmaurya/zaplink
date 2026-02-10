package io.zaplink.manager.dto.bio;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BioPageResponse
{
    private Long                  id;
    private String                username;
    @JsonProperty("owner_id")
    private String                ownerId;
    @JsonProperty("theme_config")
    private String                themeConfig;
    @JsonProperty("avatar_url")
    private String                avatarUrl;
    @JsonProperty("bio_text")
    private String                bioText;
    private String                title;
    @JsonProperty("cover_url")
    private String                coverUrl;
    @JsonProperty("seo_meta")
    private String                seoMeta;
    @JsonProperty("is_public")
    private Boolean               isPublic;
    @JsonProperty("created_at")
    private LocalDateTime         createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime         updatedAt;
    private List<BioLinkResponse> bioLinks;
}
