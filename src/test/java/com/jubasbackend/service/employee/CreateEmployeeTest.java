package com.jubasbackend.service.employee;

import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.infrastructure.entity.ProfileEntity;
import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.service.EmployeeServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEmployeeTest extends EmployeeServiceBaseTest {

    EmployeeRequest request;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> entityArgumentCaptor;

    @BeforeEach
    void setup() {
        request = new EmployeeRequest(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    @DisplayName("Deve cadastrar um funcionário com sucesso.")
    void shouldCreateEmployeeWithSuccessfully() {
        //ARRANGE
        var profile = ProfileEntity.builder().id(request.profileId()).build();
        var workingHour = WorkingHourEntity.builder().id(request.workingHourId()).build();

        var employee = new EmployeeEntity(request.profileId(), profile, workingHour, new ArrayList<>());

        doReturn(false).when(employeeRepository).existsById(uuidArgumentCaptor.capture());
        doReturn(Optional.of(profile)).when(profileRepository).findById(uuidArgumentCaptor.capture());
        doReturn(Optional.of(workingHour)).when(workingHourRepository).findById(uuidArgumentCaptor.capture());
        doReturn(employee).when(employeeRepository).save(entityArgumentCaptor.capture());

        //ACT
        var response = service.createEmployee(request);

        //ASSERT
        var capturedProfiles = uuidArgumentCaptor.getAllValues();
        var capturedEntity = entityArgumentCaptor.getValue();

        assertNotNull(response);

        assertEquals(request.profileId(), capturedProfiles.get(0));
        assertEquals(request.profileId(), capturedProfiles.get(1));
        assertEquals(request.workingHourId(), capturedProfiles.get(2));

        assertEquals(request.profileId(), capturedEntity.getId());
        assertEquals(request.profileId(), response.id());
        assertEquals(request.workingHourId(), response.workingHour().id());

        verify(employeeRepository, times(1)).existsById(capturedProfiles.get(0));
        verify(profileRepository, times(1)).findById(capturedProfiles.get(1));
        verify(workingHourRepository, times(1)).findById(capturedProfiles.get(2));
        verify(employeeRepository, times(1)).save(capturedEntity);

        verifyNoMoreInteractions(profileRepository);
        verifyNoMoreInteractions(workingHourRepository);
        verifyNoMoreInteractions(employeeRepository);

    }

    @Test
    @DisplayName("Deve ocorrer um erro caso o perfil já esteja associado a um funcionário.")
    void shouldThrowExceptionWhenProfileIsAlreadyAssociatedWithAnEmployee() {
        //ARRANGE
        doReturn(true).when(employeeRepository).existsById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createEmployee(request));

        var capturedProfileId = uuidArgumentCaptor.getValue();

        assertEquals("Profile ID already in use.", exception.getMessage());

        verify(employeeRepository, times(1)).existsById(capturedProfileId);
        verifyNoMoreInteractions(employeeRepository);
    }

    //GERAR NOVOS TESTES PARA OUTROS ERROS.
    @Test
    @DisplayName("Deve ocorrer um erro caso o perfil não exista.")
    void shouldThrowExceptionWhenProfileDoesNotExists() {
        //ARRANGE
        doReturn(false).when(employeeRepository).existsById(uuidArgumentCaptor.capture());
        doReturn(Optional.empty()).when(profileRepository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.createEmployee(request));

        var capturedProfiles = uuidArgumentCaptor.getAllValues();

        assertEquals("Profile doesn't exists.", exception.getMessage());
        assertEquals(request.profileId(), capturedProfiles.get(0));
        assertEquals(request.profileId(), capturedProfiles.get(1));

        verify(employeeRepository, times(1)).existsById(capturedProfiles.get(0));
        verify(profileRepository, times(1)).findById(capturedProfiles.get(1));

        verifyNoMoreInteractions(employeeRepository);
        verifyNoMoreInteractions(profileRepository);
    }
}
