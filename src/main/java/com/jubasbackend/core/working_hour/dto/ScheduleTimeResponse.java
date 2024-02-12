package com.jubasbackend.core.working_hour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public interface ScheduleTimeResponse {
    LocalTime time();

    boolean isAvailable();

    public record WithId(
            @Schema(type = "String", pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime time,
            boolean isAvailable,
            UUID appointmentId) implements ScheduleTimeResponse {
        public WithId(WithoutId openingHour, UUID appointmentId) {
            this(openingHour.time(), false, appointmentId);
        }
    }

    public record WithoutId(
            @Schema(type = "String", pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime time,
            boolean isAvailable) implements ScheduleTimeResponse {

        public WithoutId(ScheduleTimeResponse availableTime) {
            this(availableTime.time(), availableTime.isAvailable());
        }
    }
}
