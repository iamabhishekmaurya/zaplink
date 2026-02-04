package io.zaplink.core.dto.response.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;

public record QrAnalyticsResponse( String qrKey,
                                   String qrName,
                                   Long totalScans,
                                   Long scansToday,
                                   Long scansThisWeek,
                                   Long scansThisMonth,
                                   LocalDateTime lastScanned,
                                   List<CountryStats> countryStats,
                                   List<DeviceStats> deviceStats,
                                   List<BrowserStats> browserStats,
                                   List<DailyStats> dailyStats )
{
    public record CountryStats( String country, Long count, Double percentage )
    {
    }
    public record DeviceStats( String deviceType, Long count, Double percentage )
    {
    }
    public record BrowserStats( String browser, Long count, Double percentage )
    {
    }
    public record DailyStats( String date, Long scans )
    {
    }
}
