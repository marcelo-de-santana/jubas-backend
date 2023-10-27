package com.jubasbackend.dto;

import com.jubasbackend.domain.entity.WorkingHours;

public record WorkingHoursRequestDTO(
        String startTime,
        String endTime,
        String startInterval,
        String endInterval) {

    public WorkingHoursRequestDTO(WorkingHours workingHours) {
        this(
                workingHours.timeFormatter(workingHours.getStartTime()),
                workingHours.timeFormatter(workingHours.getEndTime()),
                workingHours.timeFormatter(workingHours.getStartInterval()),
                workingHours.timeFormatter(workingHours.getEndInterval()));
    }

}
