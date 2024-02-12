package com.jubasbackend.core.employee;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindAppointmentsByEmployeeTest extends EmployeeServiceBaseTest {

    UUID employeeId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();
    Optional<LocalDate> requestDate = Optional.of(LocalDate.now());

    ProfileEntity profileOfEmployee = ProfileEntity.builder()
            .id(employeeId).name("José Augusto").cpf("012345678910").statusProfile(true).build();

    EmployeeEntity employeeEntity = EmployeeEntity.builder().profile(profileOfEmployee).build();

    SpecialtyEntity specialty = SpecialtyEntity.builder()
            .id(specialtyId).name("Corte de cabelo").timeDuration(LocalTime.parse("00:30")).build();

    WorkingHourEntity workingHour = super.createWorkingHour("11:00", "14:00", "12:00", "13:00");

    EmployeeSpecialtyEntity compoundEntity = EmployeeSpecialtyEntity.builder()
            .employee(employeeEntity)
            .specialty(specialty).build();

    EmployeeEntity employeeOfRepository = EmployeeEntity.builder()
            .id(employeeId)
            .profile(profileOfEmployee)
            .workingHour(workingHour)
            .specialties(List.of(compoundEntity)).build();

    @Test
    @DisplayName("Deve lançar uma exceção caso o funcionário não exista.")
    void shouldThrowExceptionIfTheEmployeeDoesNotExist() {
        //ARRANGE
        doReturn(Optional.empty()).when(employeeRepository).findById(any());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findAppointmentsByEmployee(employeeId, requestDate));

        assertEquals("Employee doesn't registered.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar os horários quando não houver agendamentos.")
    void shouldReturnAppointmentsWhenThereIsNoSchedule() {
        //ARRANGE
        doReturn(Optional.of(employeeOfRepository)).when(employeeRepository).findById(uuidCaptor.capture());
        doReturn(List.of()).when(appointmentRepository).findAllByDateBetweenAndEmployeeId(localDateTimeCaptor.capture(), localDateTimeCaptor.capture(), uuidCaptor.capture());

        //ACT
        var response = service.findAppointmentsByEmployee(employeeId, requestDate);

        //ASSERT
        var capturedDateTimes = localDateTimeCaptor.getAllValues();
        var capturedId = uuidCaptor.getAllValues();

        assertNotNull(response);

        assertEquals(employeeId, capturedId.get(0));
        assertEquals(employeeId, capturedId.get(1));

        assertEquals(requestDate.get(), capturedDateTimes.get(0).toLocalDate());
        assertEquals(18, response.size(), "Eighteen times returned.");

        verify(employeeRepository, times(1)).findById(capturedId.get(0));
        verify(appointmentRepository, times(1)).findAllByDateBetweenAndEmployeeId(capturedDateTimes.get(0), capturedDateTimes.get(1), capturedId.get(1));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Deve retornar a agenda com o horário do início do agendamento marcado.")
    void shouldReturnScheduleWithScheduledStartTime() {
        //ARRANGE
        var requestDate = Optional.of(LocalDate.parse("2024-02-03"));
        var dateOfAppointment = LocalDateTime.parse("2024-02-03T13:10");
        var listOfAppointments = List.of(AppointmentEntity.builder()
                .id(UUID.randomUUID())
                .employee(employeeOfRepository)
                .specialty(specialty)
                .date(dateOfAppointment)
                .createdAt(Instant.parse("2024-02-01T12:30:45Z")).build());

        doReturn(Optional.of(employeeOfRepository)).when(employeeRepository).findById(any());
        doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeId(any(),any(), any());

        //ACT
        var response = service.findAppointmentsByEmployee(employeeId, requestDate);

        //ASSERT
        assertNotNull(response);
        assertEquals(18, response.size(), "Eighteen times returned.");

        response.forEach(appointmentResponse -> {
            var startOfAppointment = dateOfAppointment.toLocalTime();
            var availableTime = appointmentResponse.time();
            var isAvailable = appointmentResponse.isAvailable();

            if (startOfAppointment.equals(availableTime)) assertFalse(isAvailable);

        });
    }

    @Test
    @DisplayName("Deve retornar a agenda com os horários do agendamento marcados.")
    void shouldReturnScheduleWithScheduledPeriodTimes() {
        //ARRANGE
        var requestDate = Optional.of(LocalDate.parse("2024-02-03"));
        var dateOfAppointment = LocalDateTime.parse("2024-02-03T13:10");
        var appointment = AppointmentEntity.builder()
                .id(UUID.randomUUID())
                .employee(employeeOfRepository)
                .specialty(specialty)
                .date(dateOfAppointment)
                .createdAt(Instant.parse("2024-02-01T12:30:45Z")).build();

        doReturn(Optional.of(employeeOfRepository)).when(employeeRepository).findById(any());
        doReturn(List.of(appointment)).when(appointmentRepository).findAllByDateBetweenAndEmployeeId(any(),any(), any());

        //ACT
        var response = service.findAppointmentsByEmployee(employeeId, requestDate);

        //ASSERT
        assertNotNull(response);
        assertEquals(18, response.size(), "Eighteen times returned.");

        response.forEach(appointmentResponse -> {
            var startOfAppointment = dateOfAppointment.toLocalTime();
            var appointmentDuration = appointment.getSpecialty().getTimeDuration();
            var endOfAppointment = startOfAppointment
                    .plusHours(appointmentDuration.getHour()).plusMinutes(appointmentDuration.getMinute());

            var availableTime = appointmentResponse.time();
            var isAvailable = appointmentResponse.isAvailable();

            if (availableTime.equals(startOfAppointment)) assertFalse(isAvailable);
            if (availableTime.isAfter(startOfAppointment) && availableTime.isBefore(endOfAppointment))
                assertFalse(isAvailable);
        });
    }
}
