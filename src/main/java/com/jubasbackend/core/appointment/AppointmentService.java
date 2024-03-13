package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    List<ScheduleResponse> findAppointments(Optional<LocalDate> requestDate, Optional<UUID> specialtyId);

    AppointmentResponse findAppointment(UUID appointmentId);

    List<String> findDayOfAttendance();

    AppointmentEntity createAppointment(AppointmentCreateRequest request);

    void registerDaysWithoutAttendance(DaysWithoutAttendanceRequest request);

    void updateDaysOfAttendance(DaysOfAttendanceRequest request);

    void updateAppointment(UUID appointmentId, AppointmentUpdateRequest request);

    void cancelAppointment(UUID appointmentId);

    void deleteDaysWithoutAttendance(DaysWithoutAttendanceRequest request);
}
