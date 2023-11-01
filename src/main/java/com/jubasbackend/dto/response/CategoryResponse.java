package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.Category;

import java.util.List;

public record CategoryResponse(Short id, String name, List<SpecialtyMinimalResponse> specialties) {
    public CategoryResponse(Category entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getSpecialties().stream().map(SpecialtyMinimalResponse::new).toList()
        );
    }
}
