package com.jubasbackend.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record EmployeeSpecialtyRequest(
        @NotNull
        @NotBlank
        List<UUID> specialties
) {
}
