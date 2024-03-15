package com.jubasbackend.core.working_hour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public interface ScheduleTimeResponse {
    LocalTime time();

    boolean isAvailable();

     record WithId(
            @Schema(type = "String", pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime time,
            boolean isAvailable,
            UUID appointmentId) implements ScheduleTimeResponse {
        public WithId(ScheduleTimeResponse schedule, UUID appointmentId) {
            this(schedule.time(), false, appointmentId);
        }
    }

    record WithoutId(
            @Schema(type = "String", pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime time,
            boolean isAvailable) implements ScheduleTimeResponse {

        public WithoutId(ScheduleTimeResponse availableTime) {
            this(availableTime.time(), availableTime.isAvailable());
        }
    }
}
