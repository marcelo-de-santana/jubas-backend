package com.jubasbackend.service.appointment;

import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.domain.entity.*;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.exception.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAppointmentTest extends AbstractAppointmentServiceTest {

    UUID employeeId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Profile employeeProfile = Profile.builder().id(employeeId).build();
    Profile clientEntity = Profile.builder().id(clientId).build();
    Specialty specialtyEntity = Specialty.builder()
            .id(specialtyId)
            .name("Corte de cabelo")
            .timeDuration(LocalTime.parse("00:30")).build();

    WorkingHour workingHour = WorkingHour.builder()
            .id(UUID.randomUUID())
            .startTime(LocalTime.parse("09:00"))
            .endTime(LocalTime.parse("17:00"))
            .startInterval(LocalTime.parse("12:00"))
            .endInterval(LocalTime.parse("13:00")).build();

    Employee employeeEntity = Employee.builder()
            .id(employeeId)
            .profile(employeeProfile)
            .specialties(List.of(createCompoundEntity(employeeId, specialtyEntity)))
            .workingHour(workingHour)
            .build();

    AppointmentRequest request = createRequest(LocalDateTime.now());

    Appointment newAppointment = Appointment.create(request, employeeEntity);

    @Nested
    class CreateWithSuccess {

        @Test
        @DisplayName("Deve cadastrar o agendamento de um serviço.")
        void shouldCreateAnAppointment() {
            //ARRANGE
            doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(uuidCaptor.capture());
            doReturn(List.of()).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(
                    dateTimeCaptor.capture(), dateTimeCaptor.capture(), uuidCaptor.capture(), uuidCaptor.capture());
            doReturn(newAppointment).when(appointmentRepository).save(appointmentEntityCaptor.capture());

            //ACT & ASSERT
            assertNotNull(service.createAppointment(request));

            var capturedIds = uuidCaptor.getAllValues();
            var capturedDateTimes = dateTimeCaptor.getAllValues();
            var capturedEntity = appointmentEntityCaptor.getValue();

            assertEquals(request.employeeId(), capturedIds.get(0));

            assertEquals(request.dateTime().toLocalDate(), capturedDateTimes.get(0).toLocalDate());
            assertEquals(request.employeeId(), capturedIds.get(1));
            assertEquals(request.clientId(), capturedIds.get(2));

            verify(employeeRepository, times(1)).findById(capturedIds.get(0));
            verify(appointmentRepository, times(1))
                    .findAllByDateBetweenAndEmployeeIdOrClientId(
                            capturedDateTimes.get(0), capturedDateTimes.get(1), capturedIds.get(1), capturedIds.get(2));
            verify(appointmentRepository, times(1)).save(capturedEntity);

            verifyNoMoreInteractions(employeeRepository, appointmentRepository);
        }

    }

    @Nested
    class ValidateAppointment {

        @Test
        @DisplayName("Deve lançar uma exceção quando o funcionário não realiza a especialidade.")
        void shouldThrowExceptionWhenEmployeeDoesNotMakesSpecialty() {
            //ARRANGE
            var otherSpecialtyId = UUID.randomUUID();
            var request = new AppointmentRequest(employeeId, clientId, otherSpecialtyId, LocalDateTime.now());

            mockEmployeeRepositoryFindById();

            //ACT & ASSERT
            var exception = assertThrows(APIException.class,
                    () -> service.createAppointment(request));

            assertEquals("Employee doesn't makes specialty.", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

            verify(employeeRepository, times(1)).findById(any());
            verifyNoMoreInteractions(employeeRepository);
            verifyNoInteractions(appointmentRepository);
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o cliente possua agendamento pendente para o mesmo serviço no dia.")
        void shouldThrowExceptionIfTheCustomerHasAPendingAppointmentForTheSameSpecialtyOnTheDay() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(specialtyEntity)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));

            //ACT & ASSERT
            var exception = assertThrows(APIException.class,
                    () -> service.createAppointment(request));

            assertEquals("The same profile cannot schedule two services for the same day.", exception.getMessage());
        }

        @Test
        @DisplayName("Não deve lançar uma exceção em caso de serviço duplicado, porém cancelado.")
        void shouldNotThrowCaseOfDuplicateButCanceledSpecialty() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(specialtyEntity)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.CANCELADO)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.createAppointment(request));

        }

        @Test
        @DisplayName("Não deve ocorrer uma exceção se o agendamento pertencer a outro cliente.")
        void shouldNotThrowExceptionIfAppointmentBelongsToAnotherClient() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var newAppointment = Appointment.create(request, employeeEntity);
            var otherClient = Profile.builder()
                    .id(UUID.randomUUID())
                    .name("Juninho")
                    .cpf("10333333333")
                    .statusProfile(true).build();

            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(otherClient)
                    .employee(employeeEntity)
                    .specialty(specialtyEntity)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));
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
            var otherSpecialty = Specialty.builder()
                    .id(UUID.randomUUID())
                    .name("Corte de barba")
                    .timeDuration(LocalTime.parse("00:20"))
                    .price(BigDecimal.valueOf(20F)).build();

            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(otherSpecialty)
                    .createdAt(createdAt)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .date(sameDateAndTime).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));

            //ACT & ASSERT
            var exception = assertThrows(APIException.class,
                    () -> service.createAppointment(request));

            assertEquals(HttpStatus.CONFLICT,exception.getStatus());
            assertEquals("The start or end of the new schedule conflicts with the booked one.", exception.getMessage());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o fim do atendimento gere conflito com o início do próximo.")
        void shouldThrowExceptionForEndConflictWithNextStart() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:30"));
            var scheduledSpecialty = Specialty.builder()
                    .id(UUID.randomUUID())
                    .name("Corte de barba")
                    .timeDuration(LocalTime.parse("00:20"))
                    .price(BigDecimal.valueOf(20)).build();

            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(scheduledSpecialty)
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:50")).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));

            //ACT & ASSERT
            var exception = assertThrows(APIException.class, () -> service.createAppointment(request));

            assertEquals("The end time of the service must not occur after the start time of another service.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o início do atendimento sobrescreva o fim do anterior.")
        void shouldThrowExceptionIfStartOverwritesPreviousEnd() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:30"));
            var scheduledSpecialty = Specialty.builder()
                    .id(UUID.randomUUID())
                    .name("Corte de barba")
                    .timeDuration(LocalTime.parse("00:20"))
                    .price(BigDecimal.valueOf(20)).build();

            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(scheduledSpecialty)
                    .date(LocalDateTime.parse("2024-01-30T10:20"))
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .createdAt(createdAt).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));

            //ACT & ASSERT
            var exception = assertThrows(APIException.class, () -> service.createAppointment(request));

            assertEquals(HttpStatus.CONFLICT,exception.getStatus());
            assertEquals("The start time of the service must not occur before the end time of another service.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso haja agendamento no intervalo entre o início e o fim do atendimento.")
        void shouldThrowExceptionIfBookingWithinServicePeriod() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:00"));
            var scheduledSpecialty = Specialty.builder()
                    .id(UUID.randomUUID())
                    .name("Afeitamento de pezinho")
                    .timeDuration(LocalTime.parse("00:10"))
                    .price(BigDecimal.valueOf(60)).build();

            var appointmentRepositoryResponse = Appointment.builder()
                    .id(UUID.randomUUID())
                    .client(clientEntity)
                    .employee(employeeEntity)
                    .specialty(scheduledSpecialty)
                    .date(LocalDateTime.parse("2024-01-30T10:10"))
                    .appointmentStatus(AppointmentStatus.MARCADO)
                    .createdAt(createdAt).build();

            mockEmployeeRepositoryFindById();
            mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List.of(appointmentRepositoryResponse));

            //ACT
            var exception = assertThrows(APIException.class, () -> service.createAppointment(request));

            //ASSERT
            assertEquals(HttpStatus.CONFLICT,exception.getStatus());
            assertEquals("There is another appointment scheduled within the specified time period.", exception.getMessage());
        }
    }

    AppointmentRequest createRequest(LocalDateTime date) {
        return new AppointmentRequest(employeeId, clientId, specialtyId, date);
    }

    EmployeeSpecialty createCompoundEntity(UUID employeeId, Specialty specialty) {
        EmployeeSpecialty compoundEntity = EmployeeSpecialty.create(employeeId, specialty.getId());
        compoundEntity.setSpecialty(specialty);
        return compoundEntity;
    }

    void mockEmployeeRepositoryFindById() {
        doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
    }

    void mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List<Appointment> listOfAppointments) {
        doReturn(listOfAppointments).when(appointmentRepository)
                .findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
    }
}
