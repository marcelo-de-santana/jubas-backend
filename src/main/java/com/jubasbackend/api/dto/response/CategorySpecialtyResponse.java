package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.CategoryEntity;

import java.util.List;

public record CategorySpecialtyResponse(Short id, String name, List<SpecialtyResponse> specialties) {
    public CategorySpecialtyResponse(CategoryEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
