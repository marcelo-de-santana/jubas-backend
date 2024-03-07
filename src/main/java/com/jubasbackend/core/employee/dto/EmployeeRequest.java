package com.jubasbackend.core.employee.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record EmployeeRequest(
        @NotNull
        UUID profileId,
        @NotNull
        UUID workingHourId,
        @NotNull
        List<UUID> specialties) {
}

