package com.spotlight.analytics.service;

import com.spotlight.analytics.model.PlatformStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformAnalyticsServiceTest {

    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private PlatformAnalyticsService platformAnalyticsService;

    @Test
    void getPlatformStats_returnsStats() {
        PlatformStats mockStats = new PlatformStats(100L, 50L, 200L, 80L);

        when(jdbc.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn(mockStats);

        PlatformStats result = platformAnalyticsService.getPlatformStats();

        assertThat(result).isNotNull();
        assertThat(result.totalUsers()).isEqualTo(100L);
        assertThat(result.totalEvents()).isEqualTo(50L);
        assertThat(result.totalRegistrations()).isEqualTo(200L);
        assertThat(result.totalRatings()).isEqualTo(80L);
    }

    @Test
    void getPlatformStats_returnsZeros_whenEmpty() {
        PlatformStats mockStats = new PlatformStats(0L, 0L, 0L, 0L);

        when(jdbc.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn(mockStats);

        PlatformStats result = platformAnalyticsService.getPlatformStats();

        assertThat(result).isNotNull();
        assertThat(result.totalUsers()).isZero();
        assertThat(result.totalEvents()).isZero();
    }
}