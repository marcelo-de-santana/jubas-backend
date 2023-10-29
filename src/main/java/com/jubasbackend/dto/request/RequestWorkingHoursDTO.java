package com.jubasbackend.dto.request;

import com.jubasbackend.domain.entity.WorkingHours;

public record RequestWorkingHoursDTO(
        String startTime,
        String endTime,
        String startInterval,
        String endInterval) {

    public RequestWorkingHoursDTO(WorkingHours workingHours) {
        this(
                workingHours.timeFormatter(workingHours.getStartTime()),
                workingHours.timeFormatter(workingHours.getEndTime()),
                workingHours.timeFormatter(workingHours.getStartInterval()),
                workingHours.timeFormatter(workingHours.getEndInterval()));
    }

}
