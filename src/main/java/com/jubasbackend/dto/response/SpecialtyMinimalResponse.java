package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.utils.TimeUtils;

import java.util.UUID;

public record SpecialtyMinimalResponse(UUID id, String name, Float price, String timeDuration) {
    public SpecialtyMinimalResponse(Specialty specialty) {
        this(
                specialty.getId(),
                specialty.getName(),
                specialty.getPrice(),
                TimeUtils.formatTimeToString(specialty.getTimeDuration())
        );
    }
}
