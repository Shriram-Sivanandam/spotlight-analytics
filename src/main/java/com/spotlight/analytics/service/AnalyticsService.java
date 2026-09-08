package com.spotlight.analytics.service;

import com.spotlight.analytics.model.CityStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalyticsService {
    private final JdbcTemplate jdbc;

    public AnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CityStats> getEventsByCity() {
        return jdbc.query(
                """
                        SELECT city, COUNT(*) AS event_count
                        FROM events
                        WHERE deleted_at IS NULL
                          AND city IS NOT NULL
                        GROUP BY city
                        ORDER BY event_count DESC
                        LIMIT 10
                        """,
                (rs, rowNum) -> new CityStats(
                        rs.getString("city"),
                        rs.getLong("event_count")
                )
        );
    }
}
