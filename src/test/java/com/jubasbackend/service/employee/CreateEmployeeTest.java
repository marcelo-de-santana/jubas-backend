package com.jubasbackend.service.employee;

import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.ProfileEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEmployeeTest extends AbstractEmployeeServiceTest {
    @Test
    @DisplayName("Deve cadastrar um funcionário com sucesso.")
    void shouldCreateEmployeeWithSuccessfully() {
        //ARRANGE
        doReturn(Optional.of(profile)).when(profileRepository).findById(uuidCaptor.capture());
        doReturn(Optional.of(workingHour)).when(workingHourRepository).findById(uuidCaptor.capture());
        doReturn(savedEmployee).when(employeeRepository).save(employeeEntityCaptor.capture());

        //ACT
        var response = service.createEmployee(request);

        //ASSERT
        var capturedIds = uuidCaptor.getAllValues();
        var capturedEntity = employeeEntityCaptor.getValue();

        assertNotNull(response);

        assertEquals(request.profileId(), capturedIds.get(0));
        assertEquals(request.workingHourId(), capturedIds.get(1));

        assertEquals(request.profileId(), capturedEntity.getId());
        assertEquals(request.profileId(), response.id());
        assertEquals(request.workingHourId(), response.workingHour().id());
        assertEquals(request.specialties().get(0), response.specialties().get(0).id());

        verify(profileRepository, times(1)).findById(capturedIds.get(0));
        verify(workingHourRepository, times(1)).findById(capturedIds.get(1));
        verify(employeeRepository, times(1)).save(capturedEntity);

        verifyNoMoreInteractions(profileRepository, workingHourRepository, employeeRepository);

    }

    @Test
    @DisplayName("Deve ocorrer um erro caso o perfil já esteja associado a um funcionário.")
    void shouldThrowExceptionWhenProfileIsAlreadyAssociatedWithAnEmployee() {
        //ARRANGE
        var profileWithAssociatedEmployee = ProfileEntity.builder()
                .employee(EmployeeEntity.builder().build()).build();
        doReturn(Optional.of(profileWithAssociatedEmployee)).when(profileRepository).findById(any());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createEmployee(request));

        assertEquals("Profile ID already in use.", exception.getMessage());

        verifyNoMoreInteractions(profileRepository);
        verifyNoInteractions(workingHourRepository, employeeRepository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro caso o perfil não exista.")
    void shouldThrowExceptionWhenProfileDoesNotExists() {
        //ARRANGE
        doReturn(Optional.empty()).when(profileRepository).findById(any());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.createEmployee(request));

        assertEquals("Profile doesn't exists.", exception.getMessage());

        verifyNoMoreInteractions(profileRepository);
        verifyNoInteractions(workingHourRepository, employeeRepository);

    }

}
