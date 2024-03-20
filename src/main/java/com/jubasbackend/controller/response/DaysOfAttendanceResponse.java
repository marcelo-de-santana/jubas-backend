package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record DaysOfAttendanceResponse(
        @Schema(type = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        boolean isAvailable
) {
    public static DaysOfAttendanceResponse available(LocalDate date) {
        return new DaysOfAttendanceResponse(date, true);
    }

    public static DaysOfAttendanceResponse notAvailable(LocalDate date) {
        return new DaysOfAttendanceResponse(date, false);
    }
}
