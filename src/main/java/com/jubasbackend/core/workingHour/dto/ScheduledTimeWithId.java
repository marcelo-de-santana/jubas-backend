package com.jubasbackend.core.workingHour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record ScheduledTimeWithId(
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        boolean isAvailable,
        UUID appointmentId) implements ScheduleTime {
    public ScheduledTimeWithId(ScheduledTimeWithoutId openingHour, UUID appointmentId) {
        this(openingHour.time(), false, appointmentId);
    }
}
