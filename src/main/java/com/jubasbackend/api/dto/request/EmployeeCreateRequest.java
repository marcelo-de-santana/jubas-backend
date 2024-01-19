package com.jubasbackend.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmployeeCreateRequest(
        @NotNull
        @NotBlank
        UUID profileId,

        @NotNull
        @NotBlank
        UUID workingHourId
) {
}

