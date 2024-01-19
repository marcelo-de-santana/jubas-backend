package com.jubasbackend.service.employee;

import com.jubasbackend.api.dto.request.EmployeeCreateRequest;
import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.service.EmployeeServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateEmployeeTest extends EmployeeServiceBaseTest {

    EmployeeCreateRequest request;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> entityArgumentCaptor;

    @BeforeEach
    void setup() {
        request = new EmployeeCreateRequest(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    @DisplayName("Deve cadastrar um funcionário com sucesso.")
    void shouldCreateEmployeeWithSuccessfully() {
        //ARRANGE
        var employee = new EmployeeEntity(request);

        doReturn(false).when(repository).existsById(uuidArgumentCaptor.capture());
        doReturn(employee).when(repository).save(entityArgumentCaptor.capture());

        //ACT
        var response = service.createEmployee(request);

        //ASSERT
        var capturedProfileId = uuidArgumentCaptor.getValue();
        var capturedEntity = entityArgumentCaptor.getValue();

        assertNotNull(response);

        assertEquals(request.profileId(), capturedProfileId);
        assertEquals(request.profileId(), capturedEntity.getId());
        assertEquals(request.profileId(), response.id());
        assertEquals(request.workingHourId(), response.workingHour().id());

        verify(repository, times(1)).existsById(capturedProfileId);
        verify(repository, times(1)).save(capturedEntity);

        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro caso o perfil já esteja associado a um funcionário.")
    void shouldThrowExceptionWhenProfileIsAlreadyAssociatedWithAnEmployee() {
        //ARRANGE
        doReturn(true).when(repository).existsById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createEmployee(request));

        var capturedProfileId = uuidArgumentCaptor.getValue();

        assertEquals("Profile ID already in use.", exception.getMessage());

        verify(repository, times(1)).existsById(capturedProfileId);
        verifyNoMoreInteractions(repository);
    }

}
