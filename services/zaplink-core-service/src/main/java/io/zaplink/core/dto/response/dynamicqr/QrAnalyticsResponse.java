package io.zaplink.core.dto.response.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QrAnalyticsResponse( @JsonProperty("qr_key") String qrKey,
                                   @JsonProperty("qr_name") String qrName,
                                   @JsonProperty("total_scans") Long totalScans,
                                   @JsonProperty("scans_today") Long scansToday,
                                   @JsonProperty("scans_this_week") Long scansThisWeek,
                                   @JsonProperty("scans_this_month") Long scansThisMonth,
                                   @JsonProperty("last_scanned") LocalDateTime lastScanned,
                                   @JsonProperty("country_stats") List<CountryStats> countryStats,
                                   @JsonProperty("device_stats") List<DeviceStats> deviceStats,
                                   @JsonProperty("browser_stats") List<BrowserStats> browserStats,
                                   @JsonProperty("daily_stats") List<DailyStats> dailyStats )
{
    public record CountryStats( @JsonProperty("country") String country,
                                @JsonProperty("count") Long count,
                                @JsonProperty("percentage") Double percentage )
    {
    }
    public record DeviceStats( @JsonProperty("device_type") String deviceType,
                               @JsonProperty("count") Long count,
                               @JsonProperty("percentage") Double percentage )
    {
    }
    public record BrowserStats( String browser, Long count, Double percentage )
    {
    }
    public record DailyStats( String date, Long scans )
    {
    }
}
