package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.domain.entity.WorkingHour;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record WorkingHourResponse(
        UUID id,

        @Schema(type = "string", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Schema(type = "string", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        @Schema(type = "string", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startInterval,

        @Schema(type = "string", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endInterval) {

    public WorkingHourResponse(WorkingHour workingHour) {
        this(
                workingHour.getId(),
                workingHour.getStartTime(),
                workingHour.getEndTime(),
                workingHour.getStartInterval(),
                workingHour.getEndInterval()
        );
    }
}
