package com.jubasbackend.dto.request;

import java.util.UUID;

public record EmployeeRequest(UUID profileId, Long workingHourId) {
}
