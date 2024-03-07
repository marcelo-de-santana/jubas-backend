package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateEmployeeTest extends EmployeeServiceBaseTest {

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> entityArgumentCaptor;

    UUID employeeId = UUID.randomUUID();
    UUID workingHourId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();

    EmployeeRequest request = new EmployeeRequest(employeeId, workingHourId, List.of(specialtyId));

    //TODO: REFAZER TESTES
    @Test
    @DisplayName("Deve atualizar a associação com sucesso.")
    void shouldUpdateAssociationWithSuccessfully() {
        //ARRANGE
        var currentWorkingHour = WorkingHourEntity.builder().id(UUID.randomUUID()).build();
        var currentEmployee = EmployeeEntity.builder()
                .id(employeeId)
                .workingHour(currentWorkingHour).build();

        doReturn(Optional.of(currentEmployee)).when(employeeRepository).findById(uuidArgumentCaptor.capture());
        doReturn(EmployeeEntity.builder().build()).when(employeeRepository).save(entityArgumentCaptor.capture());

        //ACT
        service.updateEmployee(employeeId, request);

        //ASSERT
        var capturedEmployeeId = uuidArgumentCaptor.getValue();
        var capturedEntity = entityArgumentCaptor.getValue();

        assertEquals(employeeId, capturedEmployeeId);
        assertEquals(workingHourId, capturedEntity.getWorkingHour().getId());

        verify(employeeRepository, times(1)).findById(capturedEmployeeId);
        verify(employeeRepository, times(1)).save(capturedEntity);

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro caso o funcionário não esteja cadastrado.")
    void shouldThrowExceptionWhenEmployeeDoesNotExists() {
        //ARRANGE
        doReturn(Optional.empty()).when(employeeRepository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateEmployee(employeeId, request));

        var capturedId = uuidArgumentCaptor.getValue();

        assertEquals("Employee doesn't registered.", exception.getMessage());
        assertEquals(employeeId, capturedId);

        verify(employeeRepository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(employeeRepository);

    }
}
