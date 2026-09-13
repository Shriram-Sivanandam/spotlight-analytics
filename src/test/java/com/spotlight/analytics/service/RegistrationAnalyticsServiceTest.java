package com.spotlight.analytics.service;

import com.spotlight.analytics.model.RegistrationTrend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationAnalyticsServiceTest {
    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private RegistrationAnalyticsService registrationAnalyticsService;

    List<RegistrationTrend> mockRegistrationTrends;

    @BeforeEach
    void setUp() {
        mockRegistrationTrends = List.of(
                new RegistrationTrend(LocalDate.of(2026, 9, 1), 32L),
                new RegistrationTrend(LocalDate.of(2026, 9, 2), 30L),
                new RegistrationTrend(LocalDate.of(2026, 9, 3), 10L)
        );
    }

    @Test
    void getRegistrationTrends_returnListForGivenDays() {
        when(jdbc.query(anyString(), any(RowMapper.class), eq(3)))
                .thenReturn(mockRegistrationTrends);

        List<RegistrationTrend> result = registrationAnalyticsService.getRegistrationTrend(3);

        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(mockRegistrationTrends);
    }

    @Test
    void getRegistrationTrends_returnsEmpty_whenNoRegistrations() {
        when(jdbc.query(anyString(), any(RowMapper.class), eq(3)))
                .thenReturn(List.of());

        List<RegistrationTrend> result = registrationAnalyticsService.getRegistrationTrend(3);

        assertThat(result).isEmpty();
    }
}
