package com.jubasbackend.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.infrastructure.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record SpecialtyResponse(
        UUID id,
        String name,
        Float price,
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime timeDuration
) {
    public SpecialtyResponse(SpecialtyEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getTimeDuration()
        );
    }

    public SpecialtyResponse(EmployeeSpecialtyEntity entity) {
        this(
                entity.getSpecialty().getId(),
                entity.getSpecialty().getName(),
                entity.getSpecialty().getPrice(),
                entity.getSpecialty().getTimeDuration()
        );
    }
}
