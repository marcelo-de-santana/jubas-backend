package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.dto.AppointmentResponse;
import com.jubasbackend.core.appointment.dto.AppointmentUpdateRequest;
import com.jubasbackend.core.appointment.dto.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    List<ScheduleResponse> findAppointments(Optional<LocalDate> requestDate);

    AppointmentResponse findAppointment(UUID appointmentId);

    AppointmentEntity createAppointment(AppointmentCreateRequest request);

    void updateAppointment(UUID appointmentId, AppointmentUpdateRequest request);

    void cancelAppointment(UUID appointmentId);
}
