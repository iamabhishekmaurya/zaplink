package io.zaplink.manager.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class StatsResponse
{
    private long        totalLinks;
    private long        totalClicks;
    private long        activelinks;
    private String      topRegion;
    private double      avgCtr;
    private List<Entry> clickTrend;
    private List<Entry> referrers;
    @Data @Builder
    public static class Entry
    {
        private String name;
        private Object value; // Can be number or string depending on value type needed
    }
}
