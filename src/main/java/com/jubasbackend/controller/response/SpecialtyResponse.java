package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.domain.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.domain.entity.SpecialtyEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record SpecialtyResponse(
        UUID id,
        String name,
        Float price,
        @Schema(type = "string", pattern = "HH:mm")
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
