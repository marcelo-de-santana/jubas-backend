package com.jubasbackend.service.appointment;

import com.jubasbackend.controller.request.AppointmentCreateRequest;
import com.jubasbackend.domain.entity.AppointmentEntity;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.entity.SpecialtyEntity;
import com.jubasbackend.domain.entity.WorkingHourEntity;
import com.jubasbackend.exception.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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

    ProfileEntity employeeProfile = ProfileEntity.builder().id(employeeId).build();
    ProfileEntity clientEntity = ProfileEntity.builder().id(clientId).build();
    SpecialtyEntity specialtyEntity = SpecialtyEntity.builder()
            .id(specialtyId)
            .name("Corte de cabelo")
            .timeDuration(LocalTime.parse("00:30")).build();

    WorkingHourEntity workingHour = WorkingHourEntity.builder()
            .id(UUID.randomUUID())
            .startTime(LocalTime.parse("09:00"))
            .endTime(LocalTime.parse("17:00"))
            .startInterval(LocalTime.parse("12:00"))
            .endInterval(LocalTime.parse("13:00")).build();

    EmployeeEntity employeeEntity = EmployeeEntity.builder()
            .id(employeeId)
            .profile(employeeProfile)
            .specialties(List.of(createCompoundEntity(employeeId, specialtyEntity)))
            .workingHour(workingHour)
            .build();

    AppointmentCreateRequest request = createRequest(LocalDateTime.now());

    AppointmentEntity newAppointment = new AppointmentEntity(request, employeeEntity);

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
            var request = new AppointmentCreateRequest(employeeId, clientId, otherSpecialtyId, LocalDateTime.now());

            mockEmployeeRepositoryFindById();

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
            var appointmentRepositoryResponse = AppointmentEntity.builder()
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
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createAppointment(request));

            assertEquals("The same profile cannot schedule two services for the same day.", exception.getMessage());
        }

        @Test
        @DisplayName("Não deve lançar uma exceção em caso de serviço duplicado, porém cancelado.")
        void shouldNotThrowCaseOfDuplicateButCanceledSpecialty() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var appointmentRepositoryResponse = AppointmentEntity.builder()
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
            var newAppointment = new AppointmentEntity(request, employeeEntity);
            var otherClient = ProfileEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Juninho")
                    .cpf("10333333333")
                    .statusProfile(true).build();

            var appointmentRepositoryResponse = AppointmentEntity.builder()
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
            var otherSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Corte de barba")
                    .timeDuration(LocalTime.parse("00:20"))
                    .price(20F).build();

            var appointmentRepositoryResponse = AppointmentEntity.builder()
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
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Corte de barba")
                    .timeDuration(LocalTime.parse("00:20"))
                    .price(20F).build();

            var appointmentRepositoryResponse = AppointmentEntity.builder()
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
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Corte de barba")
                    .timeDuration(LocalTime.parse("00:20"))
                    .price(20F).build();

            var appointmentRepositoryResponse = AppointmentEntity.builder()
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
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Afeitamento de pezinho")
                    .timeDuration(LocalTime.parse("00:10"))
                    .price(60F).build();

            var appointmentRepositoryResponse = AppointmentEntity.builder()
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

    AppointmentCreateRequest createRequest(LocalDateTime date) {
        return new AppointmentCreateRequest(employeeId, clientId, specialtyId, date);
    }

    EmployeeSpecialtyEntity createCompoundEntity(UUID employeeId, SpecialtyEntity specialty) {
        EmployeeSpecialtyEntity compoundEntity = EmployeeSpecialtyEntity.create(employeeId, specialty.getId());
        compoundEntity.setSpecialty(specialty);
        return compoundEntity;
    }

    void mockEmployeeRepositoryFindById() {
        doReturn(Optional.of(employeeEntity)).when(employeeRepository).findById(any());
    }

    void mockAppointmentRepositoryFindAllWithDateEmployeeAndClient(List<AppointmentEntity> listOfAppointments) {
        doReturn(listOfAppointments).when(appointmentRepository)
                .findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
    }
}
