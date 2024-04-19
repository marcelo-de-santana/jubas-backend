package com.jubasbackend.service.schedule;

import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.domain.entity.*;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAppointmentsTest extends AbstractScheduleServiceTest {
    final static UUID CLIENT_PROFILE_ID = UUID.randomUUID();
    final static UUID EMPLOYEE_PROFILE_ID = UUID.randomUUID();
    final static UUID WORKING_HOUR_ID = UUID.randomUUID();
    final static UUID SPECIALTY_ID = UUID.randomUUID();
    final static UUID APPOINTMENT_ID = UUID.randomUUID();
    final static String EMPLOYEE_NAME = "Gabriel Navalha";
    final static LocalTime WORKING_HOUR_START_TIME = LocalTime.parse("08:00");
    final static LocalTime WORKING_HOUR_START_INTERVAL = LocalTime.parse("12:00");
    final static LocalTime WORKING_HOUR_END_INTERVAL = LocalTime.parse("13:00");
    final static LocalTime WORKING_HOUR_END_TIME = LocalTime.parse("16:00");
    final static LocalDateTime APPOINTMENT_DATE_TIME = LocalDateTime.of(2024, 3, 16, 14, 0);
    Profile CLIENT_PROFILE = Profile.builder()
            .id(CLIENT_PROFILE_ID)
            .name("José Andrade")
            .statusProfile(true)
            .build();

    Profile EMPLOYEE_PROFILE = Profile.builder()
            .id(EMPLOYEE_PROFILE_ID)
            .name(EMPLOYEE_NAME)
            .statusProfile(true)
            .build();

    WorkingHour WORKING_HOUR = WorkingHour.builder()
            .id(WORKING_HOUR_ID)
            .startTime(WORKING_HOUR_START_TIME)
            .startInterval(WORKING_HOUR_START_INTERVAL)
            .endInterval(WORKING_HOUR_END_INTERVAL)
            .endTime(WORKING_HOUR_END_TIME)
            .build();

    Specialty SPECIALTY = Specialty.builder()
            .id(SPECIALTY_ID)
            .name("Corte clássico com tesoura")
            .price(BigDecimal.valueOf(49.99))
            .timeDuration(LocalTime.parse("00:40"))
            .build();

    EmployeeSpecialtyId COMPOUND_ID = EmployeeSpecialtyId.builder()
            .employeeId(EMPLOYEE_PROFILE_ID)
            .specialtyId(SPECIALTY_ID)
            .build();

    EmployeeSpecialty COMPOUND_ENTITY = EmployeeSpecialty.builder()
            .id(COMPOUND_ID)
            .specialty(SPECIALTY)
            .build();

    Employee EMPLOYEE = Employee.builder()
            .id(EMPLOYEE_PROFILE_ID)
            .profile(EMPLOYEE_PROFILE)
            .workingHour(WORKING_HOUR)
            .specialties(List.of(COMPOUND_ENTITY))
            .build();

    Appointment APPOINTMENT = Appointment.builder()
            .id(APPOINTMENT_ID)
            .date(APPOINTMENT_DATE_TIME)
            .appointmentStatus(AppointmentStatus.MARCADO)
            .client(CLIENT_PROFILE)
            .employee(EMPLOYEE)
            .specialty(SPECIALTY)
            .createdAt(Instant.parse("2024-03-16T10:00:00Z"))
            .build();

    @Test
    @DisplayName("Deve lançar uma exceção quando nenhum funcionário estiver disponível.")
    void shouldThrowAnExceptionWhenNoEmployeeIsAvailable() {

        doReturn(List.of())
                .when(employeeRepository).findAllByActiveProfile();

        var exception = assertThrows(
                NoSuchElementException.class, () ->
                        service.getSchedule(LocalDate.now()));

        assertEquals("No employees.", exception.getMessage());

        verify(employeeRepository, times(1)).findAllByActiveProfile();
        verifyNoInteractions(appointmentRepository, nonServiceDayRepository, dayAvailabilityRepository);
    }

    @Test
    @DisplayName("Deve buscar a agenda do dia caso nenhum parâmetro seja especificado.")
    void shouldFetchTheDaysAgendaIfNoParametersAreSpecified() {
        mockReturnEmployeeRepository_FindAllByActiveProfile();
        doReturn(List.of())
                .when(appointmentRepository)
                .findAllByDateBetween(dateTimeCaptor.capture(), dateTimeCaptor.capture());

        service.getSchedule(LocalDate.now());
        var capturedDatesTimes = dateTimeCaptor.getAllValues();
        var firstDateTime = capturedDatesTimes.get(0);
        var secondDateTime = capturedDatesTimes.get(1);
        var today = LocalDate.now();

        assertEquals(today.atStartOfDay(), firstDateTime);
        assertEquals(today.atTime(23, 59, 59), secondDateTime);

    }

    @Test
    @DisplayName("Deve retornar a agenda do dia especificado.")
    void shouldReturnTheScheduleOnASpecificDay() {
        var specificDay = LocalDate.parse("2024-03-16");
        mockReturnEmployeeRepository_FindAllByActiveProfile();
        doReturn(List.of())
                .when(appointmentRepository)
                .findAllByDateBetween(dateTimeCaptor.capture(), dateTimeCaptor.capture());

        var response = service.getSchedule(specificDay);

        var capturedDatesTimes = dateTimeCaptor.getAllValues();
        var firstDateTime = capturedDatesTimes.get(0);
        var secondDateTime = capturedDatesTimes.get(1);

        assertEquals(specificDay.atStartOfDay(), firstDateTime);
        assertEquals(specificDay.atTime(23, 59, 59), secondDateTime);

    }

//    @Test
//    @DisplayName("Deve retornar a agenda com os horários indisponíveis.")
//    void shouldReturnToTheCalendarWithUnavailableTimes() {
//        mockReturnEmployeeRepository_FindAllByActiveProfile();
//        mockReturnAppointmentRepository_FindAllDateBetween(List.of(APPOINTMENT));
//
//        var response = service.getAppointments(null, null, false);
//
//        var unavailableHours = response.stream()
//                .flatMap(schedule ->
//                        schedule.workingHours()
//                                .stream()
//                                .filter(wh -> !wh.isAvailable())).toList();
//
//        assertEquals(10, unavailableHours.size());
//
//    }

    @Test
    @DisplayName("Deve retornar a agenda com os horários possíveis para realizar a especialidade.")
    void shouldReturnTheScheduleWithThePossibleTimesToCarryOutTheSpecialty() {
        mockReturnEmployeeRepository_FindAllByActiveProfile();
        mockReturnAppointmentRepository_FindAllDateBetween(List.of(APPOINTMENT));

        var response = service.getSchedule(LocalDate.now());

        var unavailableHours = response.stream()
                .flatMap(schedule ->
                        schedule.workingHours()
                                .stream()
                                .filter(wh -> !wh.isAvailable()))
                .toList();

        var impossibleTimes = response.stream()
                .flatMap(schedule ->
                        schedule.workingHours()
                                .stream()
                                .filter(this::impossibleSchedules))
                .toList();

        assertTrue(unavailableHours.isEmpty());
        assertTrue(impossibleTimes.isEmpty());
    }

    private void mockReturnEmployeeRepository_FindAllByActiveProfile() {
        doReturn(List.of(EMPLOYEE))
                .when(employeeRepository).findAllByActiveProfile();
    }

    private void mockReturnAppointmentRepository_FindAllDateBetween(List<Appointment> appointmentsReturn) {
        doReturn(appointmentsReturn)
                .when(appointmentRepository)
                .findAllByDateBetween(any(), any());

    }

    private boolean impossibleSchedules(ScheduleTimeResponse workingHours) {
        var endOfAttendance = workingHours.getTime()
                .plusMinutes(SPECIALTY
                        .getTimeDuration()
                        .getMinute());
        return (endOfAttendance.isAfter(WORKING_HOUR_START_INTERVAL) && endOfAttendance.isBefore(WORKING_HOUR_END_INTERVAL));
    }
}
