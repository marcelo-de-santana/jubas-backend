package com.jubasbackend.service.employee;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.infrastructure.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.service.EmployeeServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AddSpecialtiesTest extends EmployeeServiceBaseTest {

    UUID employeeId = UUID.randomUUID();

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> employeeEntityArgumentCaptor;

    @Captor
    ArgumentCaptor<EmployeeSpecialtyEntity> compoundEntityArgumentCaptor;

    @Test
    @DisplayName("Deve adicionar especialidades com sucesso.")
    void shouldAddSpecialtiesWithSuccessFully() {
        //ARRANGE
        var employeeId = UUID.randomUUID();
        var newSpecialties = List.of(UUID.randomUUID(), UUID.randomUUID());
        var currentEmployee = EmployeeEntity.builder()
                .id(employeeId)
                .specialties(new ArrayList<>()).build();
        var updatedCompoundEntity = EmployeeSpecialtyEntity.builder().build();

        doReturn(Optional.of(currentEmployee)).when(employeeRepository).findById(uuidArgumentCaptor.capture());
        doReturn(updatedCompoundEntity).when(employeeSpecialtyRepository).save(compoundEntityArgumentCaptor.capture());

        //ACT
        service.addSpecialties(employeeId, newSpecialties);

        //ASSERT
        var capturedEmployeeId = uuidArgumentCaptor.getValue();
        var capturedCompoundEntity = compoundEntityArgumentCaptor.getAllValues();

        assertEquals(employeeId, capturedEmployeeId);
        //CHECK EMPLOYEE
        assertEquals(employeeId, capturedCompoundEntity.get(0).getEmployee().getId());
        assertEquals(employeeId, capturedCompoundEntity.get(1).getEmployee().getId());
        //CHECK SPECIALTIES
        assertEquals(newSpecialties.get(0), capturedCompoundEntity.get(0).getSpecialty().getId());
        assertEquals(newSpecialties.get(1), capturedCompoundEntity.get(1).getSpecialty().getId());

        verify(employeeRepository, times(1)).findById(capturedEmployeeId);
        verify(employeeSpecialtyRepository, times(1)).save(capturedCompoundEntity.get(0));
        verify(employeeSpecialtyRepository, times(1)).save(capturedCompoundEntity.get(1));

        verifyNoMoreInteractions(employeeRepository);
        verifyNoMoreInteractions(employeeSpecialtyRepository);

    }

}
