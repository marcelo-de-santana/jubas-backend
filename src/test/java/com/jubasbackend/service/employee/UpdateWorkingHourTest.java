package com.jubasbackend.service.employee;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.service.EmployeeServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UpdateWorkingHourTest extends EmployeeServiceBaseTest {

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> entityArgumentCaptor;

    @Test
    @DisplayName("Deve atualizar a associação com sucesso.")
    void shouldUpdateAssociationWithSuccessfully() {
        //ARRANGE
        var employeeId = UUID.randomUUID();
        var workingHourId = UUID.randomUUID();

        var currentWorkingHour = WorkingHourEntity.builder().id(UUID.randomUUID()).build();
        var currentEmployee = EmployeeEntity.builder()
                .id(employeeId)
                .workingHour(currentWorkingHour).build();

        doReturn(Optional.of(currentEmployee)).when(employeeRepository).findById(uuidArgumentCaptor.capture());
        doReturn(EmployeeEntity.builder().build()).when(employeeRepository).save(entityArgumentCaptor.capture());

        //ACT
        service.updateWorkingHour(employeeId, workingHourId);

        //ASSERT
        var capturedEmployeeId = uuidArgumentCaptor.getValue();
        var capturedEntity = entityArgumentCaptor.getValue();

        assertEquals(employeeId, capturedEmployeeId);
        assertEquals(workingHourId, capturedEntity.getWorkingHour().getId());

        verify(employeeRepository, times(1)).findById(capturedEmployeeId);
        verify(employeeRepository, times(1)).save(capturedEntity);

        verifyNoMoreInteractions(employeeRepository);
    }
}
