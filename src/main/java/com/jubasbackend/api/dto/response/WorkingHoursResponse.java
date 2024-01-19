package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.utils.TimeUtils;

import java.util.UUID;

public record WorkingHoursResponse(
        UUID id,
        String startTime,
        String endTime,
        String startInterval,
        String endInterval) {

    public WorkingHoursResponse(WorkingHourEntity workingHours) {
        this(
                workingHours.getId(),
                TimeUtils.formatTimeToString(workingHours.getStartTime()),
                TimeUtils.formatTimeToString(workingHours.getEndTime()),
                TimeUtils.formatTimeToString(workingHours.getStartInterval()),
                TimeUtils.formatTimeToString(workingHours.getEndInterval())
        );
    }
}
