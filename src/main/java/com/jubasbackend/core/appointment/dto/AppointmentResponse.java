package com.jubasbackend.core.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String employee,
        String client,
        String specialty,
        AppointmentStatus appointmentStatus,
        @Schema(type = "String", pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime date,
        Instant createdAt,
        Instant updatedAt
) {

    public AppointmentResponse(AppointmentEntity entity) {
        this(
                entity.getId(),
                entity.getEmployee().getProfile().getName(),
                entity.getClient().getName(),
                entity.getSpecialty().getName(),
                entity.getAppointmentStatus(),
                entity.getDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );

    }
}
