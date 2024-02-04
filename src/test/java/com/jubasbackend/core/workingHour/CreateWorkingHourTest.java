package com.jubasbackend.core.workingHour;

import com.jubasbackend.exception.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateWorkingHourTest extends WorkingHourServiceBaseTest {

    @Captor
    protected ArgumentCaptor<WorkingHourEntity> entityArgumentCaptor;

    @Captor
    ArgumentCaptor<LocalTime> timeCaptor;

    WorkingHourEntity workingHour;

    @Test
    @DisplayName("Deve cadastrar nova jornada de trabalho com sucesso.")
    void shouldSuccessfullyCreateWorkingHour() {
        // ARRANGE
        var request = super.createWorkingHourRequest("09:00", "17:00", "12:00", "13:00");
        workingHour = new WorkingHourEntity(request);

        when(repository.existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                workingHour.getStartTime(),
                workingHour.getEndTime(),
                workingHour.getStartInterval(),
                workingHour.getEndInterval())).thenReturn(false);
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
        var request = super.createWorkingHourRequest("11:00", "10:59", "12:00", "13:00");

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
        var request = super.createWorkingHourRequest("11:00", "17:00", "10:59", "13:00");

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
        var request = super.createWorkingHourRequest("09:00", "12:59", "11:00", "13:00");

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class, () -> service.createWorkingHour(request));
        assertEquals("The end time cannot be before the break time.", exception.getMessage());

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Verifica a ordem dos parâmetros passados para o método e lança exceção se horário já estiver cadastrado.")
    void ChecksTheOrderOfParametersPassedToTheMethodAndThrowExceptionIfTimeAlreadyRegistered() {
        //ARRANGE
        var request = super.createWorkingHourRequest("09:00", "17:00", "12:00", "13:00");
        var workingHourOfRepository = WorkingHourEntity.builder()
                .startTime(request.startTime())
                .endTime(request.endTime())
                .startInterval(request.startInterval())
                .endInterval(request.endInterval()).build();

        doReturn(true).when(repository)
                .existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                        timeCaptor.capture(),
                        timeCaptor.capture(),
                        timeCaptor.capture(),
                        timeCaptor.capture());

        //ACT & ARRANGE
        var exception = assertThrows(ConflictException.class, () -> service.createWorkingHour(request));

        var capturedTimes = timeCaptor.getAllValues();

        assertEquals("Working hours already exists.", exception.getMessage());

        assertEquals(workingHourOfRepository.getStartTime(), capturedTimes.get(0));
        assertEquals(workingHourOfRepository.getEndTime(), capturedTimes.get(1));
        assertEquals(workingHourOfRepository.getStartInterval(), capturedTimes.get(2));
        assertEquals(workingHourOfRepository.getEndInterval(), capturedTimes.get(3));

        verify(repository).existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                capturedTimes.get(0),
                capturedTimes.get(1),
                capturedTimes.get(2),
                capturedTimes.get(3));

        verifyNoMoreInteractions(repository);
    }
}
