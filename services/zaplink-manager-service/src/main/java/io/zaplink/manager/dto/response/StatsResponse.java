package io.zaplink.manager.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class StatsResponse
{
    private long totalLinks;
    private long totalClicks;
    private long activeLinks;
}
