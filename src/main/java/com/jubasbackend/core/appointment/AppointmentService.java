package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.workingHour.dto.ScheduleTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    AppointmentEntity createAppointment(AppointmentCreateRequest request);

}
