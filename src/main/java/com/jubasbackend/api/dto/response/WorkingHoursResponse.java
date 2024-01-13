package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.WorkingHoursEntity;
import com.jubasbackend.utils.TimeUtils;

public record WorkingHoursResponse(
        Long id,
        String startTime,
        String endTime,
        String startInterval,
        String endInterval) {

    public WorkingHoursResponse(WorkingHoursEntity workingHours) {
        this(
                workingHours.getId(),
                TimeUtils.formatTimeToString(workingHours.getStartTime()),
                TimeUtils.formatTimeToString(workingHours.getEndTime()),
                TimeUtils.formatTimeToString(workingHours.getStartInterval()),
                TimeUtils.formatTimeToString(workingHours.getEndInterval())
        );
    }
}
