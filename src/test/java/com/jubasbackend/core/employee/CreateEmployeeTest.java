package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEmployeeTest extends EmployeeServiceBaseTest {


    UUID profileId = UUID.randomUUID();
    UUID workingHourId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();

    ProfileEntity profile = ProfileEntity.builder().id(profileId).name("José Navalha").statusProfile(true).build();
    SpecialtyEntity specialty = SpecialtyEntity.builder().id(specialtyId).name("Corte de cabelo").build();
    WorkingHourEntity workingHour = WorkingHourEntity.builder().id(workingHourId).build();
    EmployeeEntity savedEmployee = EmployeeEntity.builder()
            .id(profileId)
            .profile(profile)
            .workingHour(workingHour)
            .specialties(List.of(mockCompoundEntity(profile, specialty))).build();

    EmployeeRequest request = new EmployeeRequest(profileId, workingHourId, List.of(specialtyId));

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

    private EmployeeSpecialtyEntity mockCompoundEntity(ProfileEntity profile, SpecialtyEntity specialty) {
        var compoundId = new EmployeeSpecialtyId(profile.getId(), specialty.getId());
        var employeeEntity = EmployeeEntity.builder().id(profile.getId()).profile(profile).build();
        return new EmployeeSpecialtyEntity(compoundId, employeeEntity, specialty);
    }

}
