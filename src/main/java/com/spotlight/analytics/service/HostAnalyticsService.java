package com.spotlight.analytics.service;

import com.spotlight.analytics.model.HostReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HostAnalyticsService {

    private final JdbcTemplate jdbc;

    public HostAnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public HostReport getHostReport(String hostId) {
        return jdbc.queryForObject(
                """
                SELECT
                    COUNT(DISTINCT e.id) AS total_events,
                    COUNT(DISTINCT er.id) AS total_registrations,
                    COUNT(DISTINCT CASE WHEN er.status = 'accepted' THEN er.id END) AS accepted_registrations,
                    CASE
                        WHEN COUNT(DISTINCT er.id) = 0 THEN 0
                        ELSE ROUND(
                            COUNT(DISTINCT CASE WHEN er.status = 'accepted' THEN er.id END)::numeric
                            / COUNT(DISTINCT er.id) * 100, 1
                        )
                    END AS acceptance_rate,
                    COALESCE(AVG(rat.score), 0) AS average_rating
                FROM events e
                LEFT JOIN event_registrations er
                    ON er.event_id = e.id
                    AND er.deleted_at IS NULL
                LEFT JOIN event_ratings rat
                    ON rat.ratee_id = e.host_user_id
                    AND rat.rating_type = 'host'
                WHERE e.host_user_id = ?::uuid
                  AND e.deleted_at IS NULL
                """,
                (rs, rowNum) -> new HostReport(
                        rs.getLong("total_events"),
                        rs.getLong("total_registrations"),
                        rs.getLong("accepted_registrations"),
                        rs.getDouble("acceptance_rate"),
                        rs.getDouble("average_rating")
                ),
                hostId
        );
    }
}