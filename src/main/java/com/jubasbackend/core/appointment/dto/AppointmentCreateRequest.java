package com.jubasbackend.core.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentCreateRequest(
        @NotNull
        @NotBlank
        UUID employeeId,
        @NotNull
        @NotBlank
        UUID clientId,
        @NotNull
        @NotBlank
        UUID specialtyId,
        @NotNull
        @NotBlank
        AppointmentStatus appointmentStatus,
        @NotNull
        @NotBlank
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime date
) {
}
