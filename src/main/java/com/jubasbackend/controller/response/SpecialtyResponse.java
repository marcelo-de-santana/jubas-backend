package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.domain.entity.EmployeeSpecialty;
import com.jubasbackend.domain.entity.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record SpecialtyResponse(
        UUID id,
        String name,
        BigDecimal price,
        @Schema(type = "string", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime timeDuration
) {
    public SpecialtyResponse(Specialty entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getTimeDuration()
        );
    }

    public SpecialtyResponse(EmployeeSpecialty entity) {
        this(
                entity.getSpecialty().getId(),
                entity.getSpecialty().getName(),
                entity.getSpecialty().getPrice(),
                entity.getSpecialty().getTimeDuration()
        );
    }
}
