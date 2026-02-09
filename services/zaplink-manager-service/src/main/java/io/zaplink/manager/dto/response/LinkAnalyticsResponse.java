package io.zaplink.manager.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkAnalyticsResponse( @JsonProperty("short_url_key") String shortUrlKey,
                                     @JsonProperty("original_url") String originalUrl,
                                     @JsonProperty("total_clicks") Long totalClicks,
                                     @JsonProperty("clicks_today") Long clicksToday,
                                     @JsonProperty("last_accessed") LocalDateTime lastAccessed,
                                     @JsonProperty("top_countries") List<Entry> topCountries,
                                     @JsonProperty("top_browsers") List<Entry> topBrowsers,
                                     @JsonProperty("top_referrers") List<Entry> topReferrers,
                                     @JsonProperty("daily_clicks") List<Entry> dailyClicks )
{
    public record Entry( String name, Long value, Double percentage )
    {
    }
}
