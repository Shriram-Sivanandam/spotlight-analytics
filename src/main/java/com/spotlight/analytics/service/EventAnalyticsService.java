package com.spotlight.analytics.service;

import com.spotlight.analytics.model.CityStats;
import com.spotlight.analytics.model.TagStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventAnalyticsService {
    private final JdbcTemplate jdbc;

    public EventAnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CityStats> getEventsByCity() {
        return jdbc.query(
                """
                SELECT city, COUNT(*) as event_count
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

    public List<TagStats> getEventsByTag() {
        return jdbc.query(
                """
                SELECT
                    t.name           AS tag_name,
                    COUNT(et.tag_id) AS event_count
                FROM tags t
                LEFT JOIN event_tags et ON et.tag_id = t.id
                GROUP BY t.id, t.name
                ORDER BY event_count DESC
                LIMIT 10
                """,
                (rs, rowNum) -> new TagStats(
                        rs.getString("tag_name"),
                        rs.getLong("event_count")
                )
        );
    }
}
