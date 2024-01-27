package com.jubasbackend.core.specialty.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.category.dto.CategoryResponse;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record SpecialtyCategoryResponse(
        UUID id,
        String name,
        Float price,
        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime timeDuration,
        CategoryResponse category
) {
    public SpecialtyCategoryResponse(SpecialtyEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getTimeDuration(),
                new CategoryResponse(entity.getCategory())
        );
    }
}

