package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;

import java.util.List;
import java.util.UUID;

public record EmployeeSpecialtyResponse(
        UUID id,
        String name,
        List<SpecialtyResponse> specialties
) {
    public EmployeeSpecialtyResponse(EmployeeEntity entity) {
        this(
                entity.getId(),
                entity.getProfile().getName(),
                entity.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
