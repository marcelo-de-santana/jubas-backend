package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.domain.entity.AppointmentEntity;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.entity.SpecialtyEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        GenericDTO employee,
        GenericDTO client,
        GenericDTO specialty,
        AppointmentStatus status,
        Scheduling scheduling,
        Instant createdAt,
        Instant updatedAt
) {

    public AppointmentResponse(AppointmentEntity entity) {
        this(
                entity.getId(),
                new GenericDTO(entity.getEmployee()),
                new GenericDTO(entity.getClient()),
                new GenericDTO(entity.getSpecialty()),
                entity.getAppointmentStatus(),
                new Scheduling(entity),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    record GenericDTO(UUID id, String name) {
        GenericDTO(EmployeeEntity entity) {
            this(entity.getId(), entity.getProfile().getName());
        }

        GenericDTO(ProfileEntity entity) {
            this(entity.getId(), entity.getName());
        }

        GenericDTO(SpecialtyEntity specialty) {
            this(specialty.getId(), specialty.getName());
        }

    }

    record Scheduling(
            @Schema(type = "String", pattern = "yyyy-MM-dd")
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate date,
            @Schema(type = "String", pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @Schema(type = "String", pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {

        Scheduling(AppointmentEntity entity) {
            this(entity.getDate().toLocalDate(), entity.startTime(), entity.endTime());
        }
    }
}
