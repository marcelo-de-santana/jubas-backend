package com.jubasbackend.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record WorkingHourRequest(
        @NotBlank
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @NotBlank
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        @NotBlank
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime startInterval,
        @NotBlank
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime endInterval
) {
}
