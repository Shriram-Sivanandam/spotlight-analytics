package com.spotlight.analytics.model;

public record HostReport (
        Long totalEvents,
        Long totalRegistrations,
        Long acceptedRegistrations,
        Double acceptanceRate,
        Double averageRating
){}
