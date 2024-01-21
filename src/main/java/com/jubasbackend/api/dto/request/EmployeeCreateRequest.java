package com.jubasbackend.api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmployeeCreateRequest(
        @NotNull
        UUID profileId,
        @NotNull
        UUID workingHourId) {
}

