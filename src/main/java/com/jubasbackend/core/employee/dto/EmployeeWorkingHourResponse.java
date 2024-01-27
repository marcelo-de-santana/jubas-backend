package com.jubasbackend.core.employee.dto;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.workingHour.dto.WorkingHourResponse;

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
