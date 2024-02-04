package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.workingHour.dto.ScheduleTime;
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
    public ResponseEntity<Void> createAppointment(AppointmentCreateRequest request) {
        var createdAppointment = service.createAppointment(request);
        return ResponseEntity.created(URI.create("/appointment/" + createdAppointment.getId())).build();
    }
}
