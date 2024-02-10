package com.jubasbackend.core.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record AppointmentUpdateRequest(
        Optional<UUID> employeeId,
        Optional<UUID> clientId,
        Optional<UUID> specialtyId,
        @Schema(type = "String", pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        Optional<LocalDateTime> dateTime

) {
}
