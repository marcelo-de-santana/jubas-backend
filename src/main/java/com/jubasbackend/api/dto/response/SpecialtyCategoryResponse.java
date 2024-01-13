package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.utils.TimeUtils;

import java.util.UUID;

public record SpecialtyCategoryResponse(UUID id,
                                        String name,
                                        Float price,
                                        String timeDuration,
                                        CategoryResponse category) {
    public SpecialtyCategoryResponse(SpecialtyEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                TimeUtils.formatTimeToString(entity.getTimeDuration()),
                new CategoryResponse(entity.getCategory())
        );
    }
}
