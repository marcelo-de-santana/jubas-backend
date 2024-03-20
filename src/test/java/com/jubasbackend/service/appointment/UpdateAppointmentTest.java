package com.jubasbackend.service.appointment;

import com.jubasbackend.controller.request.AppointmentUpdateRequest;
import com.jubasbackend.domain.entity.AppointmentEntity;
import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.entity.SpecialtyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateAppointmentTest extends AbstractAppointmentServiceTest {

    UUID appointmentId = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();
    LocalDateTime dateTimeNow = LocalDateTime.now();

    SpecialtyEntity currentSpecialty = SpecialtyEntity.builder().id(specialtyId).build();

    EmployeeEntity currentEmployee = EmployeeEntity.builder().id(employeeId).build();

    EmployeeSpecialtyId compoundId = new EmployeeSpecialtyId(employeeId, specialtyId);
    EmployeeSpecialtyEntity compoundEntity = new EmployeeSpecialtyEntity(compoundId, currentEmployee, currentSpecialty);

    AppointmentEntity appointmentRegistered = AppointmentEntity.builder()
            .employee(currentEmployee)
            .client(ProfileEntity.builder().id(clientId).build())
            .specialty(currentSpecialty)
            .date(LocalDateTime.parse("2024-02-12T15:20")).build();

    @Test
    @DisplayName("Deve atualizar o agendamento com sucesso.")
    void shouldUpdateAppointmentWithSuccess() {
        //ARRANGE
        var input = new AppointmentUpdateRequest(employeeId, clientId, specialtyId, dateTimeNow);
        currentEmployee.setSpecialties(List.of(compoundEntity));
        AppointmentEntity appointmentRegistered = AppointmentEntity.builder()
                .employee(currentEmployee)
                .client(ProfileEntity.builder().id(clientId).build())
                .specialty(currentSpecialty)
                .date(LocalDateTime.parse("2024-02-12T15:20")).build();

        doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(uuidCaptor.capture());
        doReturn(List.of()).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
        doReturn(new AppointmentEntity()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

        //ACT & ASSERT
        assertDoesNotThrow(() -> service.updateAppointment(appointmentId, input));

        var capturedId = uuidCaptor.getValue();
        var capturedEntity = appointmentEntityCaptor.getValue();

        assertEquals(appointmentId, capturedId);

        verify(appointmentRepository, times(1)).findById(capturedId);
        verify(appointmentRepository, times(1)).save(capturedEntity);
        verifyNoMoreInteractions(appointmentRepository);
    }

    @Nested
    class ValidateEmployee {


        @Test
        @DisplayName("Deve lançar uma exceção caso o funcionário não exista.")
        void shouldThrowExceptionWhenEmployeeDoesNotExist() {
            //ARRANGE
            var otherEmployeeId = UUID.randomUUID();
            var input = new AppointmentUpdateRequest(otherEmployeeId, null, null, null);
            doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(any());
            doReturn(Optional.empty()).when(employeeRepository).findById(uuidCaptor.capture());

            //ACT & ASSERT
            var exception = assertThrows(NoSuchElementException.class, () -> service.updateAppointment(appointmentId, input));
            var capturedId = uuidCaptor.getValue();

            assertEquals("Employee doesn't registered.", exception.getMessage());
            assertEquals(otherEmployeeId, capturedId);

            verify(employeeRepository, times(1)).findById(capturedId);
            verifyNoMoreInteractions(employeeRepository);
            verifyNoMoreInteractions(appointmentRepository);
        }

        @Test
        @DisplayName("Deve atualizar o funcionário com sucesso.")
        void shouldUpdateEmployeeWithSuccess() {
            //ARRANGE
            var otherEmployeeId = UUID.randomUUID();
            var otherEmployee = EmployeeEntity.builder().id(otherEmployeeId).build();
            var compoundId = new EmployeeSpecialtyId(otherEmployeeId, specialtyId);
            var compoundEntity = new EmployeeSpecialtyEntity(compoundId, otherEmployee, currentSpecialty);
            otherEmployee.setSpecialties(List.of(compoundEntity));

            var input = new AppointmentUpdateRequest(otherEmployeeId, null, null, null);

            doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(any());
            doReturn(Optional.of(otherEmployee)).when(employeeRepository).findById(any());
            doReturn(List.of()).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
            doReturn(new AppointmentEntity()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.updateAppointment(appointmentId, input));

            var capturedEntity = appointmentEntityCaptor.getValue();

            assertEquals(otherEmployeeId, capturedEntity.getEmployee().getId());
        }

    }

    @Nested
    class ValidateSpecialty {

        @Test
        @DisplayName("Deve lançar uma exceção caso o funcionário não realize a especialidade.")
        void shouldThrowExceptionWhenEmployeeDoesNotExecuteTheSpecialty() {
            //ARRANGE
            currentEmployee.setSpecialties(List.of());
            var input = new AppointmentUpdateRequest(null, null, UUID.randomUUID(), null);

            doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(any());

            //ACT & ASSERT
            var exception = assertThrows(NoSuchElementException.class,
                    () -> service.updateAppointment(appointmentId, input));


            assertEquals("Employee doesn't execute this specialty.", exception.getMessage());
            verifyNoMoreInteractions(employeeRepository);
            verifyNoMoreInteractions(appointmentRepository);
        }

        @Test
        @DisplayName("Deve atualizar a especialidade com sucesso.")
        void shouldUpdateSpecialtyWithSuccess() {
            //ARRANGE
            currentEmployee.setSpecialties(List.of(compoundEntity));
            var appointmentRegistered = AppointmentEntity.builder()
                    .id(appointmentId)
                    .client(ProfileEntity.builder().id(clientId).build())
                    .employee(currentEmployee)
                    .date(LocalDateTime.parse("2024-02-12T15:20")).build();

            var input = new AppointmentUpdateRequest(null, null, specialtyId, null);

            doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(any());

            doReturn(new AppointmentEntity()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.updateAppointment(appointmentId, input));

            var capturedEntity = appointmentEntityCaptor.getValue();

            assertEquals(specialtyId, capturedEntity.getSpecialty().getId());
        }
    }
}
