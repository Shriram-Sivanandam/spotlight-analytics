package com.spotlight.analytics.service;

import com.spotlight.analytics.model.RegistrationTrend;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistrationAnalyticsService {

    private final JdbcTemplate jdbc;

    public RegistrationAnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<RegistrationTrend> getRegistrationTrend(int days) {
        return jdbc.query(
                """
                SELECT
                    DATE(created_at AT TIME ZONE 'UTC') AS date,
                    COUNT(*) AS registrations
                FROM event_registrations
                WHERE created_at >= NOW() - (? || ' days')::interval
                  AND deleted_at IS NULL
                GROUP BY DATE(created_at AT TIME ZONE 'UTC')
                ORDER BY date ASC
                """,
                (rs, rowNum) -> new RegistrationTrend(
                        rs.getDate("date").toLocalDate(),
                        rs.getLong("registrations")
                ),
                days
        );
    }
}