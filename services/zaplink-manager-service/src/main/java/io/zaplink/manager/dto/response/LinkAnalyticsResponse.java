package io.zaplink.manager.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class LinkAnalyticsResponse
{
    private String        shortUrlKey;
    private String        originalUrl;
    private Long          totalClicks;
    private Long          clicksToday;
    private LocalDateTime lastAccessed;
    private List<Entry>   topCountries;
    private List<Entry>   topBrowsers;
    private List<Entry>   topReferrers;
    private List<Entry>   dailyClicks;
    @Data @Builder
    public static class Entry
    {
        private String name;
        private Long   value;
        private Double percentage;
    }
}
