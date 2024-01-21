package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;

import java.util.UUID;

public record EmployeeWorkingHourResponse(
        UUID id,
        String name,
        boolean statusProfile,
        WorkingHourResponse workingHour
) {
    public EmployeeWorkingHourResponse(EmployeeEntity entity) {
        this(
                entity.getId(),
                entity.getProfile().getName(),
                entity.getProfile().isStatusProfile(),
                new WorkingHourResponse(entity.getWorkingHour())
        );
    }
}
