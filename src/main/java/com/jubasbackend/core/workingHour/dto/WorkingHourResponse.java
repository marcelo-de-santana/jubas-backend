package com.jubasbackend.core.workingHour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.workingHour.WorkingHourEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record WorkingHourResponse(
        UUID id,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startInterval,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endInterval) {

    public WorkingHourResponse(WorkingHourEntity workingHour) {
        this(
                workingHour.getId(),
                workingHour.getStartTime(),
                workingHour.getEndTime(),
                workingHour.getStartInterval(),
                workingHour.getEndInterval()
        );
    }
}
