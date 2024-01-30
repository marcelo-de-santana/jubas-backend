package com.jubasbackend.core.appointment;

import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAppointmentTest extends AppointmentServiceBaseTest {


    ProfileEntity employee = ProfileEntity.builder()
            .id(UUID.randomUUID()).name("José Augusto").cpf("12345678911").statusProfile(true).build();
    ProfileEntity client = ProfileEntity.builder()
            .id(UUID.randomUUID()).name("Fernando Gomes").cpf("12345678910").statusProfile(true).build();

    Instant createdAt = Instant.now();

    @Nested
    class CreateWithSuccess {
        @Captor
        ArgumentCaptor<UUID> uuidArgumentCaptor;

        @Captor
        ArgumentCaptor<LocalDate> dateArgumentCaptor;

        @Captor
        ArgumentCaptor<AppointmentEntity> entityArgumentCaptor;

        @Test
        @DisplayName("Deve cadastrar o agendamento de um serviço.")
        void shouldCreateAnAppointment() {
            //ARRANGE
            var appointment = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .date(LocalDateTime.now())
                    .createdAt(createdAt).build();

            doReturn(List.of()).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(dateArgumentCaptor.capture(), uuidArgumentCaptor.capture(), uuidArgumentCaptor.capture());
            doReturn(appointment).when(appointmentRepository).save(entityArgumentCaptor.capture());

            //ACT & ASSERT
            assertNotNull(service.createAppointment(appointment));

            var capturedIds = uuidArgumentCaptor.getAllValues();
            var capturedDates = dateArgumentCaptor.getAllValues();
            var capturedEntity = entityArgumentCaptor.getValue();

            assertEquals(employee.getId(), capturedIds.get(0));
            assertEquals(client.getId(), capturedIds.get(1));
            assertEquals(appointment.getDate().toLocalDate(), capturedDates.get(0));
            assertEquals(appointment, capturedEntity);

            verify(appointmentRepository, times(1)).findAllByDate_DateAndEmployeeIdOrClientId(capturedDates.get(0), capturedIds.get(0), capturedIds.get(1));
            verify(appointmentRepository, times(1)).save(capturedEntity);

            verifyNoMoreInteractions(appointmentRepository);
        }

    }

    @Nested
    class ValidateClientAppointment {

        @Test
        @DisplayName("Deve lançar uma exceção caso o cliente já possua um serviço agendado no horário.")
        void shouldThrowExceptionIfClientHasScheduledServiceAtGivenTime() {
            //ARRANGE
            var sameDateAndTime = LocalDateTime.parse("2024-01-30T10:50");
            var requestSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID()).name("Corte de cabelo").timeDuration(LocalTime.parse("00:30")).price(30F).build();
            var scheduledSpecialty = SpecialtyEntity.builder()
                    .id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var appointment = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(sameDateAndTime)
                    .specialty(requestSpecialty).build();

            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(sameDateAndTime)
                    .specialty(scheduledSpecialty)
                    .build());

            doReturn(listOfAppointments).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createAppointment(appointment));

            assertEquals("The start or end of the new schedule conflicts with the booked one.", exception.getMessage());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o cliente possua agendamento para o mesmo serviço no dia.")
        void shouldThrowExceptionForDuplicateBookingOnSameDay() {
            //ARRANGE
            var sameSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();
            var appointmentRequest = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:30"))
                    .specialty(sameSpecialty).build();
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T14:00"))
                    .specialty(sameSpecialty).build());

            doReturn(listOfAppointments).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointmentRequest));

            //ASSERT
            assertEquals("The same profile cannot schedule two services for the same day.", exception.getMessage());
        }

        @Test
        @DisplayName("Não deve ocorrer uma exceção se o cliente não tiver agendamento para o mesmo serviço no dia.")
        void shouldNotThrowExceptionIfClientHasNoBookingForTheSameServiceOnTheDay() {
            //ARRANGE
            var sameSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();
            var otherClient = ProfileEntity.builder().id(UUID.randomUUID()).name("Juninho").cpf("10333333333").statusProfile(true).build();

            var appointmentRequest = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:30"))
                    .specialty(sameSpecialty).build();
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(otherClient)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T14:00"))
                    .specialty(sameSpecialty).build());

            doReturn(listOfAppointments).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.createAppointment(appointmentRequest));

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o fim do atendimento gere conflito com o início do próximo.")
        void shouldThrowExceptionForEndConflictWithNextStart() {
            //ARRANGE
            var requestSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de cabelo").timeDuration(LocalTime.parse("00:30")).price(30F).build();
            var scheduledSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var appointmentRequest = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:30"))
                    .specialty(requestSpecialty).build();
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:50"))
                    .specialty(scheduledSpecialty).build());

            doReturn(listOfAppointments).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointmentRequest));

            //ASSERT
            assertEquals("The end time of the service must not occur after the start time of another service.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso o início do atendimento sobrescreva o fim do anterior.")
        void shouldThrowExceptionIfStartOverwritesPreviousEnd() {
            //ARRANGE
            var requestSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de cabelo").timeDuration(LocalTime.parse("00:30")).price(30F).build();
            var scheduledSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var appointmentRequest = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:30"))
                    .specialty(requestSpecialty).build();
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:20"))
                    .specialty(scheduledSpecialty).build());

            doReturn(listOfAppointments).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointmentRequest));

            //ASSERT
            assertEquals("The start time of the service must not occur before the end time of another service.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso haja agendamento no intervalo entre o início e o fim do atendimento.")
        void shouldThrowExceptionIfBookingWithinServicePeriod() {
            //ARRANGE
            var requestSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Aplicação de graxa").timeDuration(LocalTime.parse("01:00")).price(30F).build();
            var scheduledSpecialty = SpecialtyEntity.builder().id(UUID.randomUUID()).name("Corte de barba").timeDuration(LocalTime.parse("00:20")).price(20F).build();

            var appointmentRequest = AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:00"))
                    .specialty(requestSpecialty).build();
            var listOfAppointments = List.of(AppointmentEntity.builder()
                    .id(UUID.randomUUID())
                    .client(client)
                    .employee(employee)
                    .createdAt(createdAt)
                    .date(LocalDateTime.parse("2024-01-30T10:20"))
                    .specialty(scheduledSpecialty).build());

            doReturn(listOfAppointments).when(appointmentRepository).findAllByDate_DateAndEmployeeIdOrClientId(any(), any(), any());

            //ACT
            var exception = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointmentRequest));

            //ASSERT
            assertEquals("There is another appointment scheduled within the specified time period.", exception.getMessage());
    }

    }
}
