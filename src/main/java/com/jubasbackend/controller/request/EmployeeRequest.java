package com.jubasbackend.controller.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record EmployeeRequest(UUID profileId, UUID workingHourId, List<UUID> specialties) {
}

