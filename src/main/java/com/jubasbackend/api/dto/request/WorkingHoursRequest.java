package com.jubasbackend.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkingHoursRequest(
        @NotBlank
        @NotNull
        String startTime,
        @NotBlank
        @NotNull
        String endTime,
        @NotBlank
        @NotNull
        String startInterval,
        @NotBlank
        @NotNull
        String endInterval
) {
}
