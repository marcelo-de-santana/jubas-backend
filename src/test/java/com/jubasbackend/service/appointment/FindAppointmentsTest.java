package com.jubasbackend.service.appointment;

import com.jubasbackend.domain.entity.AppointmentEntity;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.entity.SpecialtyEntity;
import com.jubasbackend.domain.entity.WorkingHourEntity;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAppointmentsTest extends AbstractAppointmentServiceTest {
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
    ProfileEntity CLIENT_PROFILE = ProfileEntity.builder()
            .id(CLIENT_PROFILE_ID)
            .name("José Andrade")
            .statusProfile(true)
            .build();

    ProfileEntity EMPLOYEE_PROFILE = ProfileEntity.builder()
            .id(EMPLOYEE_PROFILE_ID)
            .name(EMPLOYEE_NAME)
            .statusProfile(true)
            .build();

    WorkingHourEntity WORKING_HOUR = WorkingHourEntity.builder()
            .id(WORKING_HOUR_ID)
            .startTime(WORKING_HOUR_START_TIME)
            .startInterval(WORKING_HOUR_START_INTERVAL)
            .endInterval(WORKING_HOUR_END_INTERVAL)
            .endTime(WORKING_HOUR_END_TIME)
            .build();

    SpecialtyEntity SPECIALTY = SpecialtyEntity.builder()
            .id(SPECIALTY_ID)
            .name("Corte clássico com tesoura")
            .price(49.99F)
            .timeDuration(LocalTime.parse("00:40"))
            .build();

    EmployeeSpecialtyId COMPOUND_ID = EmployeeSpecialtyId.builder()
            .employeeId(EMPLOYEE_PROFILE_ID)
            .specialtyId(SPECIALTY_ID)
            .build();

    EmployeeSpecialtyEntity COMPOUND_ENTITY = EmployeeSpecialtyEntity.builder()
            .id(COMPOUND_ID)
            .specialty(SPECIALTY)
            .build();

    EmployeeEntity EMPLOYEE = EmployeeEntity.builder()
            .id(EMPLOYEE_PROFILE_ID)
            .profile(EMPLOYEE_PROFILE)
            .workingHour(WORKING_HOUR)
            .specialties(List.of(COMPOUND_ENTITY))
            .build();

    AppointmentEntity APPOINTMENT = AppointmentEntity.builder()
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
                .when(employeeRepository).findAllByActiveProfile(true);

        var exception = assertThrows(
                NoSuchElementException.class, () ->
                        service.findAppointments(null, null, false));

        assertEquals("No employees.", exception.getMessage());

        verify(employeeRepository, times(1)).findAllByActiveProfile(true);
        verifyNoInteractions(appointmentRepository, nonServiceDayRepository, dayAvailabilityRepository);
    }

    @Test
    @DisplayName("Deve buscar a agenda do dia caso nenhum parâmetro seja especificado.")
    void shouldFetchTheDaysAgendaIfNoParametersAreSpecified() {
        mockReturnEmployeeRepository_FindAllByActiveProfile();
        doReturn(List.of())
                .when(appointmentRepository)
                .findAllByDateBetween(dateTimeCaptor.capture(), dateTimeCaptor.capture());

        service.findAppointments(null, null, false);
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

        var response = service.findAppointments(specificDay, null, false);

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
//        var response = service.findAppointments(null, null, false);
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

        var response = service.findAppointments(null, SPECIALTY_ID, false);

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
                .when(employeeRepository).findAllByActiveProfile(true);
    }

    private void mockReturnAppointmentRepository_FindAllDateBetween(List<AppointmentEntity> appointmentsReturn) {
        doReturn(appointmentsReturn)
                .when(appointmentRepository)
                .findAllByDateBetween(any(), any());

    }

    private boolean impossibleSchedules(ScheduleTimeResponse workingHours) {
        var endOfAttendance = workingHours.time()
                .plusMinutes(SPECIALTY
                        .getTimeDuration()
                        .getMinute());
        return (endOfAttendance.isAfter(WORKING_HOUR_START_INTERVAL) && endOfAttendance.isBefore(WORKING_HOUR_END_INTERVAL));
    }
}
