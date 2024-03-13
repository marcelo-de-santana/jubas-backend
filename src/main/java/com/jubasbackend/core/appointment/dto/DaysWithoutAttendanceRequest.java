package com.jubasbackend.core.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record DaysWithoutAttendanceRequest(
        @Schema(type = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        List<LocalDate> dates
) {
}
