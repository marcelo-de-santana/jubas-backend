package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private ProfileEntity employee;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ProfileEntity client;
    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private SpecialtyEntity specialty;
    private AppointmentStatus appointmentStatus;
    private LocalDateTime date;
    private Instant createdAt;
    private Instant updatedAt;

    public AppointmentEntity(AppointmentCreateRequest request, EmployeeSpecialtyEntity compoundEntity) {
        this.client = ProfileEntity.builder().id(request.clientId()).build();
        this.employee = compoundEntity.getEmployee().getProfile();
        this.specialty = compoundEntity.getSpecialty();
        this.appointmentStatus = request.appointmentStatus();
        this.date = request.date();
        this.createdAt = Instant.now();
    }

    public LocalTime startTime() {
        return getDate().toLocalTime();
    }

    public LocalTime endTime() {
        var duration = getSpecialty().getTimeDuration();
        var startTime = startTime();
        return startTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute());
    }

    public boolean isInThePeriod(LocalTime time) {
        return (time.equals(startTime()) || (time.isAfter(startTime()) && time.isBefore(endTime())));
    }
}
