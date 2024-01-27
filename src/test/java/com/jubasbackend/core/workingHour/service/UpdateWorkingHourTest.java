package com.jubasbackend.core.workingHour.service;

import com.jubasbackend.core.workingHour.WorkingHourEntity;
import com.jubasbackend.core.workingHour.dto.WorkingHourRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateWorkingHourTest extends WorkingHourServiceBaseTest {

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    UUID workingHourId;
    WorkingHourRequest request;

    @BeforeEach
    void setup() {
        workingHourId = UUID.randomUUID();
        request = WorkingHourRequest.create("09:00", "17:00", "12:00", "13:00");
    }

    @Test
    @DisplayName("Deve atualizar jornada de trabalho com sucesso.")
    void shouldUpdateWorkingHourSuccessfully() {
        //ARRANGE
        var existingWorkingHour = WorkingHourEntity.builder().id(workingHourId).build();
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
        var request = WorkingHourRequest.create("11:00", "10:59", "12:00", "13:00");
        var existingWorkingHour = WorkingHourEntity.builder().id(workingHourId).build();
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
}
