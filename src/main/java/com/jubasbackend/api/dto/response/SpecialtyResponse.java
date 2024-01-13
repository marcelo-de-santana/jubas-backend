package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.utils.TimeUtils;

import java.util.UUID;

public record SpecialtyResponse(UUID id, String name, Float price, String timeDuration) {
    public SpecialtyResponse(SpecialtyEntity specialty) {
        this(
                specialty.getId(),
                specialty.getName(),
                specialty.getPrice(),
                TimeUtils.formatTimeToString(specialty.getTimeDuration())
        );
    }
}
