package com.spotlight.analytics.model;
import java.time.LocalDate;

public record RegistrationTrend (
        LocalDate Date,
        Long registration
) {}
