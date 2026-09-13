package com.spotlight.analytics.service;

import com.spotlight.analytics.model.HostReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HostAnalyticsServiceTest {
    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private HostAnalyticsService hostAnalyticsService;

    private HostReport mockHostReport;

    @BeforeEach
    void setUp() {
        mockHostReport = new HostReport(15L, 30L, 25L, 80.0, 3.42);
    }

    @Test
    void getHostReport_returnsReportForValidHost() {
        when(jdbc.queryForObject(anyString(), any(RowMapper.class), eq("test-uuid")))
                .thenReturn(mockHostReport);

        HostReport result = hostAnalyticsService.getHostReport("test-uuid");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockHostReport);
    }

    @Test
    void getHostReport_returnsNullForInvalidHost() {
        when(jdbc.queryForObject(anyString(), any(RowMapper.class), eq("invalid-uuid")))
                .thenReturn(null);

        HostReport result = hostAnalyticsService.getHostReport("invalid-uuid");

        assertThat(result).isNull();
    }

    @Test
    void getHostReport_throwsException_whenHostNotFound() {
        when(jdbc.queryForObject(anyString(), any(RowMapper.class), eq("invalid-uuid")))
                .thenThrow(new EmptyResultDataAccessException(1));

        assertThatThrownBy(() -> hostAnalyticsService.getHostReport("invalid-uuid"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
