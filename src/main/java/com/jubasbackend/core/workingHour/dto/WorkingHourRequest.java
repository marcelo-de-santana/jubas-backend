package com.jubasbackend.core.workingHour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import static java.time.LocalTime.parse;

public record WorkingHourRequest(
        @NotNull
        @NotBlank
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @NotNull
        @NotBlank
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        @NotNull
        @NotBlank
        LocalTime startInterval,
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        @NotNull
        @NotBlank
        LocalTime endInterval
) {

        public static WorkingHourRequest create(String startTime, String endTime, String startInterval, String endInterval) {
                return new WorkingHourRequest(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
        }

}
