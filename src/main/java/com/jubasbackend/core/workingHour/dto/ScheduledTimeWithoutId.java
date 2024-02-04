package com.jubasbackend.core.workingHour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ScheduledTimeWithoutId(
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        boolean isAvailable) implements ScheduleTime {

    public ScheduledTimeWithoutId(ScheduleTime availableTime) {
        this(availableTime.time(), availableTime.isAvailable());
    }
}
