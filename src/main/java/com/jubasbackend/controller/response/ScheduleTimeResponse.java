package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ScheduleTimeResponse {

    @Schema(type = "String", pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    final LocalTime time;
    final boolean isAvailable;

    public ScheduleTimeResponse(ScheduleTimeResponse availableTime) {
        this(availableTime.getTime(), availableTime.isAvailable());
    }

    @Getter
    public static class WithAppointmentId extends ScheduleTimeResponse {
        UUID appointmentId;

        public WithAppointmentId(ScheduleTimeResponse schedule, UUID appointmentId) {
            super(schedule.getTime(), false);
            this.appointmentId = appointmentId;
        }
    }

}
