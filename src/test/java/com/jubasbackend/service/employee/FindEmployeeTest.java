package com.jubasbackend.service.employee;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.WorkingHour;
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
        var employee = Employee.builder()
                .id(employeeId)
                .profile(Profile.builder().id(UUID.randomUUID()).build())
                .workingHour(WorkingHour.builder().id(UUID.randomUUID()).build())
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
