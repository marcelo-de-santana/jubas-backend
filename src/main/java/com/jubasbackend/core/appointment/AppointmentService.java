package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    List<ScheduleResponse> findAppointments(LocalDate requestDate, UUID specialtyId, boolean filtered);

    AppointmentResponse findAppointment(UUID appointmentId);

    List<DaysOfAttendanceResponse> findDaysOfAttendance(Optional<LocalDate> startDate, Optional<LocalDate> endDate);

    AppointmentEntity createAppointment(AppointmentCreateRequest request);

    void registerDaysWithoutAttendance(List<LocalDate> dates);

    void updateRangeOfAttendanceDays(RangeOfAttendanceRequest request);

    void updateAppointment(UUID appointmentId, AppointmentUpdateRequest request);

    void cancelAppointment(UUID appointmentId);

    void deleteDaysWithoutAttendance(List<LocalDate> dates);
}
