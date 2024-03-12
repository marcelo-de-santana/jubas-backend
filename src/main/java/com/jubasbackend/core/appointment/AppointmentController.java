package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AppointmentController implements AppointmentApi {

    private final AppointmentService service;

    @Override
    public ResponseEntity<List<ScheduleResponse>> findAppointments(Optional<LocalDate> date, Optional<UUID> specialtyId) {
        return ResponseEntity.ok(service.findAppointments(date, specialtyId));
    }

    @Override
    public ResponseEntity<AppointmentResponse> findAppointment(UUID appointmentId) {
        return ResponseEntity.ok(service.findAppointment(appointmentId));
    }

    @Override
    public ResponseEntity<List<String>> findDaysOfAttendance() {
        return ResponseEntity.ok(service.findDayOfAttendance());
    }

    @Override
    public ResponseEntity<Void> createAppointment(AppointmentCreateRequest request) {
        var createdAppointment = service.createAppointment(request);
        return ResponseEntity.created(URI.create("/appointments/" + createdAppointment.getId())).build();
    }

    @Override
    public ResponseEntity<Void> updateDaysOfAttendance(DaysOfAttendanceRequest request) {
        service.updateDaysOfAttendance(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateAppointment(UUID appointmentId, AppointmentUpdateRequest request) {
        service.updateAppointment(appointmentId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelAppointment(UUID appointmentId) {
        service.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
