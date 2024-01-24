package com.jubasbackend.service.workingHour;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.service.WorkingHourServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static java.time.LocalTime.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateWorkingHourTest extends WorkingHourServiceBaseTest {

    @Captor
    protected ArgumentCaptor<WorkingHourEntity> entityArgumentCaptor;
    WorkingHourRequest request;
    WorkingHourEntity workingHour;

    @Test
    @DisplayName("Deve cadastrar nova jornada de trabalho com sucesso.")
    void shouldSuccessfullyCreateWorkingHour() {
        // ARRANGE
        request = newWorkingHourRequest("09:00", "17:00", "12:00", "13:00");
        workingHour = new WorkingHourEntity(request);

        when(service.areTimesRegisteredOnRepository(workingHour)).thenReturn(false);
        when(repository.save(entityArgumentCaptor.capture())).thenReturn(workingHour);

        // ACT & ASSERT
        var response = service.createWorkingHour(request);

        var capturedEntity = entityArgumentCaptor.getValue();

        assertNotNull(response);
        assertEquals(request.startTime(), response.startTime());
        assertEquals(request.endTime(), response.endTime());
        assertEquals(request.startInterval(), response.startInterval());
        assertEquals(request.endInterval(), response.endInterval());

        verify(repository, times(1)).save(capturedEntity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o início da jornada de trabalho for maior que o final.")
    void shouldThrowExceptionWhenStartOfWorkdayIsGreaterThanEnd() {
        //ARRANGE
        request = newWorkingHourRequest("11:00", "10:59", "12:00", "13:00");

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createWorkingHour(request));

        assertEquals("The start time of the working day cannot be less than the end time.", exception.getMessage());

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o início é depois do intervalo.")
    void shouldThrowExceptionWhenStartTimeIsAfterStartInterval() {
        //ARRANGE
        request = newWorkingHourRequest("11:00", "17:00", "10:59", "13:00");

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createWorkingHour(request));
        assertEquals("The start time cannot be less than the break time.", exception.getMessage());

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o final é antes do intervalo.")
    void shouldThrowExceptionWhenEndTimeIsBeforeEndInterval() {
        //ARRANGE
        var request = newWorkingHourRequest("09:00", "12:59", "11:00", "13:00");

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class, () -> service.createWorkingHour(request));
        assertEquals("The end time cannot be before the break time.", exception.getMessage());

        verifyNoInteractions(repository);
    }

}
