package com.jubasbackend.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentCreateRequest(
        @NotNull
        UUID employeeId,
        @NotNull
        UUID clientId,
        @NotNull
        UUID specialtyId,
        @NotNull
        @Schema(type = "String", pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime dateTime
) {
        public LocalTime time() {
                return dateTime.toLocalTime();
        }

        public LocalDate date(){
                return dateTime.toLocalDate();
        }
}
