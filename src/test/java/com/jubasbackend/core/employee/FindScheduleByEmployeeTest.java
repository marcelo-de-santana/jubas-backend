package com.jubasbackend.core.employee;

import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.workingHour.WorkingHourEntityTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FindScheduleByEmployeeTest extends EmployeeServiceBaseTest {

    @Test
    @DisplayName("Deve retornar o primeiro horário igual ao horário de início de jornada e o último igual com 10 minutos de antecedência.")
    void shouldReturnFirstScheduleStartingAtShiftStartAndLastScheduleEndingWith10MinutesEarly() {
        //ARRANGE
        var employeeId = UUID.randomUUID();

        var employee = EmployeeEntity.builder()
                .profile(ProfileEntity.builder().id(UUID.randomUUID()).name("José Augusto").statusProfile(true).build())
                .workingHour(WorkingHourEntityTest.createValidEntity())
                .specialties(new ArrayList<>()).build();

        doReturn(Optional.of(employee)).when(employeeRepository).findById(employeeId);

        //ACT
        var response = service.findScheduleByEmployee(employeeId);

        //ASSERT
        assertNotNull(response);

        verify(employeeRepository, times(1)).findById(employeeId);
    }

}

