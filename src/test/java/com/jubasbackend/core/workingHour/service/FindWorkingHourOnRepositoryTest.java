package com.jubasbackend.core.workingHour.service;

import com.jubasbackend.core.workingHour.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindWorkingHourOnRepositoryTest extends WorkingHourServiceBaseTest {

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Test
    @DisplayName("Deve retorna jornada de trabalho com sucesso.")
    void shouldReturnWorkingHourSuccessfully() {
        //ARRANGE
        var workingHourId = UUID.randomUUID();
        var workingHour = WorkingHourEntity.builder().id(workingHourId).build();
        doReturn(Optional.of(workingHour)).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT
        var response = service.findWorkingHourOnRepository(workingHourId);

        //ASSERT
        var capturedId = uuidArgumentCaptor.getValue();

        assertNotNull(response);
        assertEquals(workingHourId, capturedId);

        verify(repository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Deve lançar uma exceção ao tentar encontrar uma jornada de trabalho não registrada.")
    void shouldThrowNoSuchElementExceptionWhenWorkingHourNotRegistered() {
        //ARRANGE
        var unregisteredWorkingHourId = UUID.randomUUID();
        doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findWorkingHourOnRepository(unregisteredWorkingHourId));

        assertEquals("Unregistered working hours.", exception.getMessage());

        var capturedId = uuidArgumentCaptor.getValue();
        assertEquals(unregisteredWorkingHourId, capturedId);

        verify(repository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(repository);
    }
}
