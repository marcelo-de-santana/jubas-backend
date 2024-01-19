package com.jubasbackend.service.employee;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.infrastructure.entity.ProfileEntity;
import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.service.EmployeeServiceBaseTest;
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

public class FindEmployeeTest extends EmployeeServiceBaseTest {

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
        doReturn(Optional.of(employee)).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT
        var response = service.findEmployee(employeeId);

        //ASSERT
        var capturedId = uuidArgumentCaptor.getValue();

        assertNotNull(response);
        assertEquals(employeeId, capturedId);

        verify(repository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(repository);
    }


}
