package com.jubasbackend.service.working_hour;

import com.jubasbackend.domain.entity.WorkingHour;
import com.jubasbackend.controller.request.WorkingHourRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateWorkingHourTest extends AbstractWorkingHourServiceTest {

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    UUID workingHourId = UUID.randomUUID();
    WorkingHourRequest request = super.createWorkingHourRequest("09:00", "17:00", "12:00", "13:00");

    WorkingHour existingWorkingHour = WorkingHour.builder().id(workingHourId).build();

    @Test
    @DisplayName("Deve atualizar jornada de trabalho com sucesso.")
    void shouldUpdateWorkingHourSuccessfully() {
        //ARRANGE
        doReturn(Optional.of(existingWorkingHour)).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT
        assertDoesNotThrow(() -> service.updateWorkingHour(workingHourId, request));

        //ASSERT
        var capturedId = uuidArgumentCaptor.getValue();
        assertEquals(workingHourId, capturedId);

        verify(repository, times(1)).findById(capturedId);
        verify(repository, times(1)).save(existingWorkingHour);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar atualizar com horários inválidos")
    void shouldThrowExceptionWhenUpdatingWithInvalidTimes() {
        //ARRANGE
        var workingHourId = UUID.randomUUID();
        var request = createWorkingHourRequest("11:00", "10:59", "12:00", "13:00");

        doReturn(Optional.of(existingWorkingHour)).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateWorkingHour(workingHourId, request));

        assertEquals("The start time of the working day cannot be less than the end time.", exception.getMessage());

        var capturedId = uuidArgumentCaptor.getValue();
        assertEquals(workingHourId, capturedId);

        verify(repository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar encontrar uma jornada de trabalho não registrada.")
    void shouldThrowNoSuchElementExceptionWhenWorkingHourNotRegistered() {
        //ARRANGE
        doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateWorkingHour(workingHourId, request));

        assertEquals("Unregistered working hours.", exception.getMessage());

        var capturedId = uuidArgumentCaptor.getValue();
        assertEquals(workingHourId, capturedId);

        verify(repository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(repository);
    }

}
