package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.exception.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    ProfileEntity employeeOfRequest = ProfileEntity.builder().id(employeeId).build();
    ProfileEntity clientOfRequest = ProfileEntity.builder().id(clientId).build();

    EmployeeEntity employeeEntityOfRequest = EmployeeEntity.builder()
            .profile(employeeOfRequest).build();

    SpecialtyEntity specialtyOfRequest = SpecialtyEntity.builder()
            .id(specialtyId).name("Corte de cabelo").timeDuration(LocalTime.parse("00:30")).build();

    EmployeeSpecialtyEntity compoundEntity = EmployeeSpecialtyEntity.builder()
            .employee(employeeEntityOfRequest)
            .specialty(specialtyOfRequest).build();

    AppointmentCreateRequest createRequest(LocalDateTime date) {
        return new AppointmentCreateRequest(employeeId, clientId, specialtyId, AppointmentStatus.MARCADO, date);
    }

    @Nested
    class CreateWithSuccess {

        @Test
        @DisplayName("Deve cadastrar o agendamento de um serviço.")
        void shouldCreateAnAppointment() {
            //ARRANGE
            var request = createRequest(LocalDateTime.now());
            var ofResult = Optional.of(compoundEntity);

            doReturn(ofResult).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(uuidCaptor.capture(), uuidCaptor.capture());
            doReturn(List.of()).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(localDateTimeCaptor.capture(), uuidCaptor.capture(), uuidCaptor.capture());
            doReturn(new AppointmentEntity()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

            //ACT & ASSERT
            assertNotNull(service.createAppointment(request));

            var capturedIds = uuidCaptor.getAllValues();
            var capturedDateTimes = localDateTimeCaptor.getAllValues();
            var capturedEntity = appointmentEntityCaptor.getValue();

            assertEquals(request.employeeId(), capturedIds.get(0));
            assertEquals(request.specialtyId(), capturedIds.get(1));

            assertEquals(request.date().toLocalDate(), capturedDateTimes.get(0));
            assertEquals(request.employeeId(), capturedIds.get(2));
            assertEquals(request.clientId(), capturedIds.get(3));

            verify(employeeSpecialtyRepository, times(1)).findByEmployeeIdAndSpecialtyId(capturedIds.get(0), capturedIds.get(1));
            verify(appointmentRepository, times(1)).findAllByDateAndEmployeeIdOrClientId(capturedDateTimes.get(0), capturedIds.get(2), capturedIds.get(3));
            verify(appointmentRepository, times(1)).save(capturedEntity);

            verifyNoMoreInteractions(appointmentRepository);
        }

    }

    @Nested
    class ValidateAppointment {

        @Test
        @DisplayName("Deve lançar uma exceção caso o cliente possua agendamento para o mesmo serviço no dia.")
        void shouldThrowExceptionForDuplicateBookingOnSameDay() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(clientOfRequest)
                    .employee(employeeOfRequest)
                    .specialty(specialtyOfRequest)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build());

            doReturn(Optional.of(compoundEntity)).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(any(), any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createAppointment(request));

            //ASSERT
            assertEquals("The same profile cannot schedule two services for the same day.", exception.getMessage());
        }

        @Test
        @DisplayName("Não deve ocorrer uma exceção se o agendamento pertencer a outro cliente.")
        void shouldNotThrowExceptionIfAppointmentBelongsToAnotherClient() {
            //ARRANGE
            var request = createRequest(LocalDateTime.parse("2024-01-30T10:50"));
            var otherClient = ProfileEntity.builder()
                    .id(UUID.randomUUID()).name("Juninho").cpf("10333333333").statusProfile(true).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(otherClient)
                    .employee(employeeOfRequest)
                    .specialty(specialtyOfRequest)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T14:00")).build());

            doReturn(Optional.of(compoundEntity)).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(any(), any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(any(), any(), any());

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
                    .client(clientOfRequest)
                    .employee(employeeOfRequest)
                    .specialty(otherSpecialty)
                    .createdAt(createdAt)
                    .date(sameDateAndTime).build());

            doReturn(Optional.of(compoundEntity)).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(any(), any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(any(), any(), any());

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
                    .client(clientOfRequest)
                    .employee(employeeOfRequest)
                    .specialty(scheduledSpecialty)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:50")).build());

            doReturn(Optional.of(compoundEntity)).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(any(), any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(any(), any(), any());

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
                    .client(clientOfRequest)
                    .employee(employeeOfRequest)
                    .specialty(scheduledSpecialty)
                    .date(LocalDateTime.parse("2024-01-30T10:20"))
                    .createdAt(createdAt).build());

            doReturn(Optional.of(compoundEntity)).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(any(), any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(any(), any(), any());

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
                    .client(clientOfRequest)
                    .employee(employeeOfRequest)
                    .specialty(scheduledSpecialty)
                    .date(LocalDateTime.parse("2024-01-30T10:10"))
                    .createdAt(createdAt).build());

            doReturn(Optional.of(compoundEntity)).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(any(), any());
            doReturn(listOfAppointments).when(appointmentRepository).findAllByDateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(ConflictException.class, () -> service.createAppointment(request));

            //ASSERT
            assertEquals("There is another appointment scheduled within the specified time period.", exception.getMessage());
        }
    }

    @Nested
    class ValidateEmployeeSpecialty {
        @Test
        @DisplayName("Deve lançar uma exceção se a especialidade não for encontrada para o funcionário.")
        void shouldThrowAnExceptionIfTheSpecialtyIsNotFoundForTheEmployee() {
            //ARRANGE
            var request = createRequest(LocalDateTime.now());

            doReturn(Optional.empty()).when(employeeSpecialtyRepository).findByEmployeeIdAndSpecialtyId(employeeId, specialtyId);

            //ACT & ASSERT
            assertThrows(NoSuchElementException.class,
                    () -> service.createAppointment(request),
                    "Employee doesn't perform the specialty.");

            verify(employeeSpecialtyRepository, times(1)).findByEmployeeIdAndSpecialtyId(employeeId, specialtyId);
            verifyNoMoreInteractions(employeeSpecialtyRepository);
            verifyNoInteractions(appointmentRepository);
        }
    }
}
