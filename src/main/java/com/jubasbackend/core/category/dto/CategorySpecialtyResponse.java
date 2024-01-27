package com.jubasbackend.core.category.dto;

import com.jubasbackend.core.category.CategoryEntity;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;

import java.util.List;

public record CategorySpecialtyResponse(Short id, String name, List<SpecialtyResponse> specialties) {
    public CategorySpecialtyResponse(CategoryEntity category) {
        this(
                category.getId(),
                category.getName(),
                category.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
