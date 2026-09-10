package com.spotlight.analytics.controller;

import com.spotlight.analytics.model.*;
import com.spotlight.analytics.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final EventAnalyticsService eventService;
    private final HostAnalyticsService hostAnalyticsService;
    private final RegistrationAnalyticsService registrationService;
    private final PlatformAnalyticsService platformService;

    public AnalyticsController(EventAnalyticsService eventService, HostAnalyticsService hostAnalyticsService, RegistrationAnalyticsService registrationService, PlatformAnalyticsService platformService) {
        this.eventService = eventService;
        this.hostAnalyticsService = hostAnalyticsService;
        this.registrationService = registrationService;
        this.platformService = platformService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/events/by-city")
    public List<CityStats> getEventsByCity() {
        return eventService.getEventsByCity();
    }

    @GetMapping("/tags/popular")
    public List<TagStats> getEventsByTag() {
        return eventService.getEventsByTag();
    }

    @GetMapping("/hosts/{hostId}/report")
    public ResponseEntity<HostReport> getHostReport(@PathVariable("hostId") String hostId) {
        try {
            return ResponseEntity.ok(hostAnalyticsService.getHostReport(hostId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/registrations/trend")
    public List<RegistrationTrend> getRegistrationTrend(
            @RequestParam(defaultValue = "30") int days
    ) {
        return registrationService.getRegistrationTrend(days);
    }

    @GetMapping("/platform/stats")
    public PlatformStats getPlatformStats() {
        return platformService.getPlatformStats();
    }
}
