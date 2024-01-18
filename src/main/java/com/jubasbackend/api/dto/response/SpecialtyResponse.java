package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.utils.TimeUtils;

import java.util.UUID;

public record SpecialtyResponse(UUID id,
                                String name,
                                Float price,
                                String timeDuration) {
    public SpecialtyResponse(SpecialtyEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                TimeUtils.formatTimeToString(entity.getTimeDuration())
        );
    }
}
