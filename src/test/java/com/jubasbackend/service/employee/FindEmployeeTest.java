package com.jubasbackend.service.employee;

import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.entity.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FindEmployeeTest extends AbstractEmployeeServiceTest {

    UUID employeeId = UUID.randomUUID();

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Test
    @DisplayName("Deve buscar o funcionário com sucesso.")
    void shouldFindEmployeeWithSuccessfully() {
        //ARRANGE
        var employee = EmployeeEntity.builder()
                .id(employeeId)
                .profile(ProfileEntity.builder().id(UUID.randomUUID()).build())
                .workingHour(WorkingHourEntity.builder().id(UUID.randomUUID()).build())
                .specialties(new ArrayList<>()).build();
        doReturn(Optional.of(employee)).when(employeeRepository).findById(uuidArgumentCaptor.capture());

        //ACT
        var response = service.findEmployee(employeeId);

        //ASSERT
        var capturedId = uuidArgumentCaptor.getValue();

        assertNotNull(response);
        assertEquals(employeeId, capturedId);

        verify(employeeRepository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(employeeRepository);
    }


}
