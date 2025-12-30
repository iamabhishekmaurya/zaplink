package io.zaplink.manager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data @Entity @Table(name = "url_statistics")
public class UrlStatisticsEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false)
    private Long          id;
    @Column(name = "short_url_key", nullable = false, unique = true)
    private String        shortUrlKey;
    @Column(name = "total_clicks", nullable = false)
    private Long          totalClicks = 0L;
    @Column(name = "today_clicks", nullable = false)
    private Long          todayClicks = 0L;
    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
