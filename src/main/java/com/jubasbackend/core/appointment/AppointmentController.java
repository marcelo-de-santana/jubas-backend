package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ScheduleResponse>> findAppointments(LocalDate date, UUID specialtyId, boolean filtered) {
        return ResponseEntity.ok(service.findAppointments(date, specialtyId, filtered));
    }

    @Override
    public ResponseEntity<AppointmentResponse> findAppointment(UUID appointmentId) {
        return ResponseEntity.ok(service.findAppointment(appointmentId));
    }

    @Override
    public ResponseEntity<List<DaysOfAttendanceResponse>> findDaysOfAttendance(
            Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        return ResponseEntity.ok(service.findDaysOfAttendance(startDate, endDate));
    }

    @Override
    public ResponseEntity<Void> createAppointment(AppointmentCreateRequest request) {
        var createdAppointment = service.createAppointment(request);
        return ResponseEntity.created(URI.create("/appointments/" + createdAppointment.getId())).build();
    }

    @Override
    public ResponseEntity<Void> registerDaysWithoutAttendance(List<LocalDate> dates) {
        service.registerDaysWithoutAttendance(dates);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updateRangeOfAttendanceDays(RangeOfAttendanceRequest request) {
        service.updateRangeOfAttendanceDays(request);
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

    @Override
    public ResponseEntity<Void> deleteDaysWithoutAttendance(List<LocalDate> dates) {
        service.deleteDaysWithoutAttendance(dates);
        return ResponseEntity.noContent().build();
    }
}
