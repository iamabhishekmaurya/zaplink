package io.zaplink.manager.dto.response;

import java.time.LocalDateTime;
import io.zaplink.manager.common.enums.UrlStatusEnum;
import lombok.Builder;
import lombok.Data;
import io.zaplink.manager.dto.RedirectRuleDto;
import java.util.List;

@Data @Builder
public class LinkResponse
{
    private Long                  id;
    private String                shortUrlKey;
    private String                originalUrl;
    private String                shortUrl;
    private LocalDateTime         createdAt;
    private Long                  clickCount;
    private UrlStatusEnum         status;
    private List<RedirectRuleDto> rules;
    private List<String>          tags;
}
