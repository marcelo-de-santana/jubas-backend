package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import com.jubasbackend.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAppointmentTest extends AppointmentServiceBaseTest {

    UUID employeeId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();
    Instant createdAt = Instant.parse("2024-01-20T10:00:00Z");

    ProfileEntity employeeProfile = ProfileEntity.builder().id(employeeId).build();
    ProfileEntity clientEntity = ProfileEntity.builder().id(clientId).build();
    SpecialtyEntity specialtyEntity = SpecialtyEntity.builder()
            .id(specialtyId).name("Corte de cabelo").timeDuration(LocalTime.parse("00:30")).build();

    WorkingHourEntity workingHour = WorkingHourEntity.builder()
            .id(UUID.randomUUID())
            .startTime(LocalTime.parse("09:00"))
            .endTime(LocalTime.parse("17:00"))
            .startInterval(LocalTime.parse("12:00"))
            .endInterval(LocalTime.parse("13:00")).build();

    EmployeeSpecialtyId compoundId = new EmployeeSpecialtyId(employeeId, specialtyId);

    EmployeeEntity employeeEntity = EmployeeEntity.builder()
            .id(employeeId).profile(employeeProfile).workingHour(workingHour).build();

    EmployeeSpecialtyEntity compoundEntity = new EmployeeSpecialtyEntity(compoundId, employeeEntity, specialtyEntity);

    AppointmentCreateRequest createRequest(LocalDateTime date) {
        return new AppointmentCreateRequest(employeeId, clientId, specialtyId, date);
    }

    @Nested
    class CreateWithSuccess {

        @Test
        @DisplayName("Deve cadastrar o agendamento de um serviço.")
        void shouldCreateAnAppointment() {
            //ARRANGE
            var request = createRequest(LocalDateTime.now());
            employeeEntity.setSpecialties(List.of(compoundEntity));
            var newAppointment = new AppointmentEntity(request, employeeEntity);

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(uuidCaptor.capture());
            doReturn(List.of()).when(appointmentRepository)
                    .findAllByDateBetweenAndEmployeeIdOrClientId(localDateTimeCaptor.capture(), localDateTimeCaptor.capture(), uuidCaptor.capture(), uuidCaptor.capture());
            doReturn(newAppointment).when(appointmentRepository).save(appointmentEntityCaptor.capture());

            //ACT & ASSERT
            assertNotNull(service.createAppointment(request));

            var capturedIds = uuidCaptor.getAllValues();
            var capturedDateTimes = localDateTimeCaptor.getAllValues();
            var capturedEntity = appointmentEntityCaptor.getValue();

            assertEquals(request.employeeId(), capturedIds.get(0));

            assertEquals(request.dateTime().toLocalDate(), capturedDateTimes.get(0).toLocalDate());
            assertEquals(request.employeeId(), capturedIds.get(1));
            assertEquals(request.clientId(), capturedIds.get(2));

            verify(employeeRepository, times(1)).findById(capturedIds.get(0));
            verify(appointmentRepository, times(1))
                    .findAllByDateBetweenAndEmployeeIdOrClientId(capturedDateTimes.get(0), capturedDateTimes.get(1), capturedIds.get(1), capturedIds.get(2));
            verify(appointmentRepository, times(1)).save(capturedEntity);

            verifyNoMoreInteractions(appointmentRepository);
        }

    }

    @Nested
    class ValidateAppointment {

        @BeforeEach
        void setUp() {
            employeeEntity.setSpecialties(List.of(compoundEntity));
        }

        @Test
        @DisplayName("Deve lançar uma exceção quando o funcionário não realiza a especialidade.")
        void shouldThrowExceptionWhenEmployeeDoesNotMakesSpecialty() {
            //ARRANGE
            var request = new AppointmentCreateRequest(employeeId, clientId, UUID.randomUUID(), LocalDateTime.now());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());

            //ACT & ASSERT
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createAppointment(request));

            assertEquals("Employee doesn't makes specialty.", exception.getMessage());

            verify(employeeRepository, times(1)).findById(any());
            verifyNoMoreInteractions(employeeRepository);
            verifyNoInteractions(appointmentRepository);
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o cliente possua agendamento pendente para o mesmo serviço no dia.")
        void shouldThrowExceptionIfTheCustomerHasAPendingAppointmentForTheSameSpecialtyOnTheDay() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(specialtyEntity)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());

            //ACT & ASSERT
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createAppointment(request));

            assertEquals("The same profile cannot schedule two services for the same day.", exception.getMessage());
        }

        @Test
        @DisplayName("Não deve lançar uma exceção em caso de serviço duplicado, porém cancelado.")
        void shouldNotThrowCaseOfDuplicateButCanceledSpecialty() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(specialtyEntity)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.CANCELADO)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.createAppointment(request));

        }

        @Test
        @DisplayName("Não deve ocorrer uma exceção se o agendamento pertencer a outro cliente.")
        void shouldNotThrowExceptionIfAppointmentBelongsToAnotherClient() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var newAppointment = new AppointmentEntity(request, employeeEntity);
            var otherClient = ProfileEntity.builder()
                    .id(UUID.randomUUID()).name("Juninho").cpf("10333333333").statusProfile(true).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(otherClient)
                    .employee(employeeEntity)
                    .specialty(specialtyEntity)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
            doReturn(newAppointment).when(appointmentRepository).save(any());

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.createAppointment(request));
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o cliente já possua um serviço agendado no horário.")
        void shouldThrowExceptionIfClientHasScheduledServiceAtGivenTime() {
            //ARRANGE
            var sameDateAndTime = LocalDateTime.now();
            var request = createRequest(sameDateAndTime);
            var otherSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(otherSpecialty)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .date(sameDateAndTime).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());

            //ACT & ASSERT
            var exception = assertThrows(ConflictException.class,
                    () -> service.createAppointment(request));

            assertEquals("The start or end of the new schedule conflicts with the booked one.", exception.getMessage());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o fim do atendimento gere conflito com o início do próximo.")
        void shouldThrowExceptionForEndConflictWithNextStart() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:30"));
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(scheduledSpecialty)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:50")).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());

            //ACT & ASSERT
            var exception = assertThrows(ConflictException.class, () -> service.createAppointment(request));

            assertEquals("The end time of the service must not occur after the start time of another service.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o início do atendimento sobrescreva o fim do anterior.")
        void shouldThrowExceptionIfStartOverwritesPreviousEnd() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:30"));
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(scheduledSpecialty)
                    .date(LocalDateTime.parse("2024-01-30T10:20"))
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .createdAt(createdAt).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());

            //ACT & ASSERT
            var exception = assertThrows(ConflictException.class, () -> service.createAppointment(request));

            assertEquals("The start time of the service must not occur before the end time of another service.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso haja agendamento no intervalo entre o início e o fim do atendimento.")
        void shouldThrowExceptionIfBookingWithinServicePeriod() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:00"));
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID()).name("Afeitamento de pezinho").timeDuration(LocalTime.parse("00:10")).price(60F).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(scheduledSpecialty)
                    .date(LocalDateTime.parse("2024-01-30T10:10"))
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .createdAt(createdAt).build());

            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());

            //ACT
            var exception = assertThrows(ConflictException.class, () -> service.createAppointment(request));

            //ASSERT
            assertEquals("There is another appointment scheduled within the specified time period.", exception.getMessage());
        }
    }

}
