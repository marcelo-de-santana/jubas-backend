package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.WorkingHours;

public record ResponseWorkingHoursDTO(
        Long id,
        String startTime,
        String endTime,
        String startInterval,
        String endInterval) {

    public ResponseWorkingHoursDTO(WorkingHours workingHours) {
        this(
                workingHours.getId(),
                workingHours.timeFormatter(workingHours.getStartTime()),
                workingHours.timeFormatter(workingHours.getEndTime()),
                workingHours.timeFormatter(workingHours.getStartInterval()),
                workingHours.timeFormatter(workingHours.getEndInterval()));
    }
}
