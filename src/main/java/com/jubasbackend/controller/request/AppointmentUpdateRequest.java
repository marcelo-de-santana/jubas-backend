package com.jubasbackend.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentUpdateRequest(
        UUID employeeId,
        UUID clientId,
        UUID specialtyId,
        @Schema(type = "String", pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime dateTime

) {
}
