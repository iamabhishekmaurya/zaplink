package io.zaplink.manager.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatsResponse( @JsonProperty("total_links") long totalLinks,
                             @JsonProperty("total_clicks") long totalClicks,
                             @JsonProperty("active_links") long activeLinks,
                             @JsonProperty("top_region") String topRegion,
                             @JsonProperty("avg_ctr") double avgCtr,
                             @JsonProperty("click_trend") List<Entry> clickTrend,
                             @JsonProperty("referrers") List<Entry> referrers )
{
    public record Entry( @JsonProperty("name") String name, @JsonProperty("value") Object value )
    {
    }
}
