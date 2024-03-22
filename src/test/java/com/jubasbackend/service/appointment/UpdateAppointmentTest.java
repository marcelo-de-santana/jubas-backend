package com.jubasbackend.service.appointment;

import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.EmployeeSpecialty;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.Specialty;
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

    Specialty currentSpecialty = Specialty.builder().id(specialtyId).build();

    Employee currentEmployee = Employee.builder().id(employeeId).build();

    EmployeeSpecialtyId compoundId = new EmployeeSpecialtyId(employeeId, specialtyId);
    EmployeeSpecialty compoundEntity = new EmployeeSpecialty(compoundId, currentEmployee, currentSpecialty);

    Appointment appointmentRegistered = Appointment.builder()
            .employee(currentEmployee)
            .client(Profile.builder().id(clientId).build())
            .specialty(currentSpecialty)
            .date(LocalDateTime.parse("2024-02-12T15:20")).build();

    @Test
    @DisplayName("Deve atualizar o agendamento com sucesso.")
    void shouldUpdateAppointmentWithSuccess() {
        //ARRANGE
        var input = new AppointmentRequest(employeeId, clientId, specialtyId, dateTimeNow);
        currentEmployee.setSpecialties(List.of(compoundEntity));
        Appointment appointmentRegistered = Appointment.builder()
                .employee(currentEmployee)
                .client(Profile.builder().id(clientId).build())
                .specialty(currentSpecialty)
                .date(LocalDateTime.parse("2024-02-12T15:20")).build();

        doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(uuidCaptor.capture());
        doReturn(List.of()).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
        doReturn(new Appointment()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

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
            var input = new AppointmentRequest(otherEmployeeId, null, null, null);
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
            var otherEmployee = Employee.builder().id(otherEmployeeId).build();
            var compoundId = new EmployeeSpecialtyId(otherEmployeeId, specialtyId);
            var compoundEntity = new EmployeeSpecialty(compoundId, otherEmployee, currentSpecialty);
            otherEmployee.setSpecialties(List.of(compoundEntity));

            var input = new AppointmentRequest(otherEmployeeId, null, null, null);

            doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(any());
            doReturn(Optional.of(otherEmployee)).when(employeeRepository).findById(any());
            doReturn(List.of()).when(appointmentRepository).findAllByDateBetweenAndEmployeeIdOrClientId(any(), any(), any(), any());
            doReturn(new Appointment()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

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
            var input = new AppointmentRequest(null, null, UUID.randomUUID(), null);

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
            var appointmentRegistered = Appointment.builder()
                    .id(appointmentId)
                    .client(Profile.builder().id(clientId).build())
                    .employee(currentEmployee)
                    .date(LocalDateTime.parse("2024-02-12T15:20")).build();

            var input = new AppointmentRequest(null, null, specialtyId, null);

            doReturn(Optional.of(appointmentRegistered)).when(appointmentRepository).findById(any());

            doReturn(new Appointment()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

            //ACT & ASSERT
            assertDoesNotThrow(() -> service.updateAppointment(appointmentId, input));

            var capturedEntity = appointmentEntityCaptor.getValue();

            assertEquals(specialtyId, capturedEntity.getSpecialty().getId());
        }
    }
}
