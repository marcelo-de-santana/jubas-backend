package com.jubasbackend.api.dto.request;

import java.util.UUID;

public record EmployeeRequest(UUID profileId, Long workingHourId) {
}
