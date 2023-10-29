package com.jubasbackend.dto.response;

import com.jubasbackend.domain.projection.CategorySpecialtyProjection;

import java.util.UUID;

public record ResponseMinimalSpecialtyDTO(UUID id, String name, Float price, String timeDuration) {
    public ResponseMinimalSpecialtyDTO(CategorySpecialtyProjection projection) {
        this(
                projection.getSpecialtyId(),
                projection.getSpecialtyName(),
                projection.getSpecialtyPrice(),
                projection.getSpecialtyTimeDuration()
        );
    }
}
