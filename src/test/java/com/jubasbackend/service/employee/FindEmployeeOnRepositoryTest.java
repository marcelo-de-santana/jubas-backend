package com.jubasbackend.service.employee;

import com.jubasbackend.service.EmployeeServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FindEmployeeOnRepositoryTest extends EmployeeServiceBaseTest {

    UUID employeeId = UUID.randomUUID();

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Test
    @DisplayName("Deve ocorrer um erro caso o funcionário não esteja cadastrado.")
    void shouldThrowExceptionWhenEmployeeDoesNotExists() {
        //ARRANGE
        doReturn(Optional.empty()).when(employeeRepository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findEmployeeOnRepository(employeeId));

        var capturedId = uuidArgumentCaptor.getValue();

        assertEquals("Employee doesn't registered.", exception.getMessage());
        assertEquals(employeeId, capturedId);

        verify(employeeRepository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(employeeRepository);

    }
}
