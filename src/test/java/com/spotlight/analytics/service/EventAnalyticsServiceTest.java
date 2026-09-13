package com.spotlight.analytics.service;

import com.spotlight.analytics.model.CityStats;
import com.spotlight.analytics.model.TagStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventAnalyticsServiceTest {
    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private EventAnalyticsService eventAnalyticsService;

    private List<CityStats> mockCityStats;
    private List<TagStats> mockTagStats;

    @BeforeEach
    void setUp() {
        mockCityStats = List.of(
                new CityStats("Mumbai", 15L),
                new CityStats("Chennai", 12L),
                new CityStats("Pune", 19L)
        );
        mockTagStats = List.of(
                new TagStats("Gaming", 12L),
                new TagStats("Sports", 33L)
        );
    }

    @Test
    void getEventsByCity_returnsListOfCityStats() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(mockCityStats);

        List<CityStats> result = eventAnalyticsService.getEventsByCity();

        assertThat(result).hasSize(mockCityStats.size());
        assertThat(result).isEqualTo(mockCityStats);
    }

    @Test
    void getEventsByCity_returnsEmptyList_whenNoEvents() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of());

        List<CityStats> result = eventAnalyticsService.getEventsByCity();

        assertThat(result).isEmpty();
    }

    @Test
    void getEventsByTag_returnListOfTagStats() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(mockTagStats);

        List<TagStats> result = eventAnalyticsService.getEventsByTag();

        assertThat(result).hasSize(mockTagStats.size());
        assertThat(result).isEqualTo(mockTagStats);
    }

    @Test
    void getEventsByTag_returnsEmptyList_whenNoEvents() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of());

        List<TagStats> result = eventAnalyticsService.getEventsByTag();

        assertThat(result).isEmpty();
    }
}
