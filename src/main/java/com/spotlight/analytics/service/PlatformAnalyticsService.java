package com.spotlight.analytics.service;

import com.spotlight.analytics.model.PlatformStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlatformAnalyticsService {

    private final JdbcTemplate jdbc;

    public PlatformAnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public PlatformStats getPlatformStats() {
        return jdbc.queryForObject(
                """
                SELECT
                    (SELECT COUNT(*) FROM users
                     WHERE is_anonymized = false)        AS total_users,
                    (SELECT COUNT(*) FROM events
                     WHERE deleted_at IS NULL)           AS total_events,
                    (SELECT COUNT(*) FROM event_registrations
                     WHERE deleted_at IS NULL)           AS total_registrations,
                    (SELECT COUNT(*) FROM event_ratings) AS total_ratings
                """,
                (rs, rowNum) -> new PlatformStats(
                        rs.getLong("total_users"),
                        rs.getLong("total_events"),
                        rs.getLong("total_registrations"),
                        rs.getLong("total_ratings")
                )
        );
    }
}