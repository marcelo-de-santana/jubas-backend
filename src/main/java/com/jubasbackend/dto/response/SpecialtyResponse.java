package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.utils.TimeUtils;

import java.util.UUID;

public record SpecialtyResponse(UUID id,
                                String name,
                                Float price,
                                String timeDuration,
                                CategoryMinimalResponse category) {
    public SpecialtyResponse(Specialty entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                TimeUtils.formatTimeToString(entity.getTimeDuration()),
                new CategoryMinimalResponse(entity.getCategory())
        );
    }
}
