package com.jubasbackend.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AppointmentResponse {

    final UUID id;
    final GenericDTO employee;
    final GenericDTO client;
    final SpecialtyResponse specialty;
    final AppointmentStatus status;
    final Scheduling scheduling;
    final Instant createdAt;
    final Instant updatedAt;

    public AppointmentResponse(Appointment entity) {
        this(
                entity.getId(),
                new GenericDTO(entity.getEmployee()),
                new GenericDTO(entity.getClient()),
                new SpecialtyResponse(entity.getSpecialty()),
                entity.getAppointmentStatus(),
                new Scheduling(entity),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    record GenericDTO(UUID id, String name) {
        GenericDTO(Employee entity) {
            this(entity.getId(), entity.getProfile().getName());
        }

        GenericDTO(Profile entity) {
            this(entity.getId(), entity.getName());
        }

    }

    record Scheduling(
            @Schema(type = "string", pattern = "yyyy-MM-dd") @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate date,

            @Schema(type = "string", pattern = "HH:mm") @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,

            @Schema(type = "string", pattern = "HH:mm") @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {

        Scheduling(Appointment entity) {
            this(entity.getDate().toLocalDate(), entity.startTime(), entity.endTime());
        }
    }

}
