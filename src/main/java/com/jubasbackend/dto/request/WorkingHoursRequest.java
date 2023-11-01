package com.jubasbackend.dto.request;

public record WorkingHoursRequest(
        String startTime,
        String endTime,
        String startInterval,
        String endInterval
) {
}
