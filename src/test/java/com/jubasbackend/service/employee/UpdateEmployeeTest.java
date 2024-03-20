package com.jubasbackend.service.employee;

import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.controller.request.EmployeeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateEmployeeTest extends AbstractEmployeeServiceTest {
    @Test
    @DisplayName("Deve adicionar a especialidade ao funcionário.")
    void shouldAddSpecialtyToEmployee() {
        //ARRANGE
        var newSpecialtyId = UUID.randomUUID();
        var request = new EmployeeRequest(profileId, workingHourId, List.of(newSpecialtyId));

        doReturn(Optional.of(savedEmployee)).when(employeeRepository).findById(uuidCaptor.capture());
        doReturn(new EmployeeEntity()).when(employeeRepository).save(employeeEntityCaptor.capture());

        //ACT
        service.updateEmployee(employeeId, request);

        //ASSERT
        var capturedId = uuidCaptor.getValue();
        var capturedEntity = employeeEntityCaptor.getValue();

        assertEquals(employeeId, capturedId);
        assertEquals(newSpecialtyId, capturedEntity.getSpecialties().get(1).getId().getSpecialtyId());

        verify(employeeRepository, times(1)).findById(capturedId);
        verify(employeeRepository, times(1)).save(capturedEntity);

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Deve remover a especialidade se já estiver associada ao funcionário.")
    void shouldRemoveSpecialtyIfAlreadyAssociated() {
        //ARRANGE
        var request = new EmployeeRequest(profileId, workingHourId, List.of(specialtyId));

        doReturn(Optional.of(savedEmployee)).when(employeeRepository).findById(any());
        doReturn(new EmployeeEntity()).when(employeeRepository).save(employeeEntityCaptor.capture());

        //ACT
        service.updateEmployee(employeeId, request);

        //ASSERT
        var capturedEntity = employeeEntityCaptor.getValue();
        assertEquals(0, capturedEntity.getSpecialties().size());
    }

    @Test
    @DisplayName("Deve atualizar a jornada de trabalho do funcionário com sucesso.")
    void shouldUpdateEmployeeWorkingHourSuccessfully() {
        //ARRANGE
        var newWorkingHourId = UUID.randomUUID();
        var request = new EmployeeRequest(profileId, newWorkingHourId, List.of(specialtyId));

        doReturn(Optional.of(savedEmployee)).when(employeeRepository).findById(any());
        doReturn(new EmployeeEntity()).when(employeeRepository).save(employeeEntityCaptor.capture());

        //ACT
        service.updateEmployee(employeeId, request);

        //ASSERT
        var capturedEntity = employeeEntityCaptor.getValue();
        assertEquals(newWorkingHourId, capturedEntity.getWorkingHour().getId());
        verifyNoMoreInteractions(employeeRepository);
    }


    @Test
    @DisplayName("Deve ocorrer um erro caso o funcionário não esteja cadastrado.")
    void shouldThrowExceptionWhenEmployeeDoesNotExists() {
        //ARRANGE
        doReturn(Optional.empty()).when(employeeRepository).findById(any());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateEmployee(employeeId, request));

        assertEquals("Employee doesn't registered.", exception.getMessage());

        verifyNoMoreInteractions(employeeRepository);
    }
}
