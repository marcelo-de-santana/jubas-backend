package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleResponse {

    @Schema(type = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    final LocalDate date;
    final boolean isAvailable;

    public static ScheduleResponse available(LocalDate date) {
        return new ScheduleResponse(date, true);
    }

    public static ScheduleResponse notAvailable(LocalDate date) {
        return new ScheduleResponse(date, false);
    }

    @Getter
    public static class WithEmployees extends ScheduleResponse {
        List<EmployeeScheduleTimeResponse> employees;

        public WithEmployees(LocalDate date, List<EmployeeScheduleTimeResponse> employees) {
            super(date, true);
            this.employees = employees;
        }


    }

}

