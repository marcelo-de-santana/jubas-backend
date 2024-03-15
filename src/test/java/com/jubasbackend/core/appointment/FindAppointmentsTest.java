package com.jubasbackend.core.appointment;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAppointmentsTest extends AppointmentServiceBaseTest {
    final static UUID CLIENT_PROFILE_ID = UUID.randomUUID();
    final static UUID EMPLOYEE_PROFILE_ID = UUID.randomUUID();
    final static UUID WORKING_HOUR_ID = UUID.randomUUID();
    final static UUID SPECIALTY_ID = UUID.randomUUID();

    final static ProfileEntity CLIENT_PROFILE = ProfileEntity.builder()
            .id(CLIENT_PROFILE_ID)
            .name("José Andrade")
            .statusProfile(true)
            .build();

    final static ProfileEntity EMPLOYEE_PROFILE = ProfileEntity.builder()
            .id(EMPLOYEE_PROFILE_ID)
            .name("Gabriel Navalha")
            .statusProfile(true)
            .build();

    WorkingHourEntity WORKING_HOUR = WorkingHourEntity.builder()
            .id(WORKING_HOUR_ID)
            .startTime(LocalTime.parse("08:00"))
            .startInterval(LocalTime.parse("12:00"))
            .endInterval(LocalTime.parse("13:00"))
            .endTime(LocalTime.parse("16:00"))
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


    @Test
    @DisplayName("Deve lançar uma exceção quando nenhum funcionário estiver disponível.")
    void shouldThrowAnExceptionWhenNoEmployeeIsAvailable() {

        doReturn(List.of(EmployeeEntity.builder()
                .id(EMPLOYEE_PROFILE_ID)
                .profile(ProfileEntity.builder()
                        .id(EMPLOYEE_PROFILE_ID)
                        .name("Gabriel Navalha")
                        .statusProfile(false)
                        .build())
                .build()))
                .when(employeeRepository).findAll();

        var exception = assertThrows(
                NoSuchElementException.class, () ->
                        service.findAppointments(null, null, false));

        assertEquals("No employees.", exception.getMessage());

        verify(employeeRepository, times(1)).findAll();
        verifyNoInteractions(appointmentRepository, nonServiceDayRepository, dayAvailabilityRepository);
    }

    @Test
    @DisplayName("Deve buscar a agenda do dia caso nenhum parâmetro seja especificado.")
    void shouldFetchTheDaysAgendaIfNoParametersAreSpecified() {
        mockReturnEmployeeRepository_FindAll();
        doReturn(List.of())
                .when(appointmentRepository)
                .findAllByDateBetween(dateTimeCaptor.capture(), dateTimeCaptor.capture());

        service.findAppointments(null, null, false);
        var capturedDatesTimes = dateTimeCaptor.getAllValues();
        var firstDate = capturedDatesTimes.get(0).toLocalDate();
        var secondDate = capturedDatesTimes.get(1).toLocalDate();
        var today = LocalDate.now();

        assertEquals(today, firstDate);
        assertEquals(today, secondDate);

    }

    @Test
    @DisplayName("Deve retornar a agenda do dia caso nenhum parâmetro seja especificado e nenhum atendimento esteja registrado.")
    void shouldReturnTheDaysAgendaWhenNoParametersArePassedAndNoServiceIsRegistered() {
        mockReturnEmployeeRepository_FindAll();
        mockReturnAppointmentRepository_FindAllDateBetween(List.of());

        var response = service.findAppointments(null, null, false);

        var firstOfSchedule = response.get(0);
        var firstWorkingHour = firstOfSchedule.workingHours().get(0);
        var lastWorkingHour = firstOfSchedule.workingHours().get(firstOfSchedule.workingHours().size() - 1);

        assertEquals(LocalTime.parse("08:00"), firstWorkingHour.time());
        assertEquals(LocalTime.parse("15:50"), lastWorkingHour.time());
        assertTrue(firstWorkingHour.isAvailable());
        assertTrue(lastWorkingHour.isAvailable());

    }

    @Test
    void deveRetornarSomenteOsHorariosDisponíveisParaRealizarAEspecialidade(){
        //A ESPECIALIDADE É PASSADA E SOMENTE OS HORÁRIOS QUE SE ENCAIXAM NA ESPECIALIDADE SERÃO RETORNADOS
    }


    private void mockReturnEmployeeRepository_FindAll() {
        doReturn(List.of(EMPLOYEE))
                .when(employeeRepository).findAll();
    }

    private void mockReturnAppointmentRepository_FindAllDateBetween(List<AppointmentEntity> appointmentsReturn) {
        doReturn(appointmentsReturn)
                .when(appointmentRepository)
                .findAllByDateBetween(any(), any());

    }
}
