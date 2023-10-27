package com.jubasbackend.dto;

import com.jubasbackend.domain.entity.WorkingHours;

public record WorkingHoursResponseDTO(
        Long id,
        String startTime,
        String endTime,
        String startInterval,
        String endInterval) {

    public WorkingHoursResponseDTO(WorkingHours workingHours) {
        this(
                workingHours.getId(),
                workingHours.timeFormatter(workingHours.getStartTime()),
                workingHours.timeFormatter(workingHours.getEndTime()),
                workingHours.timeFormatter(workingHours.getStartInterval()),
                workingHours.timeFormatter(workingHours.getEndInterval()));
    }
}
