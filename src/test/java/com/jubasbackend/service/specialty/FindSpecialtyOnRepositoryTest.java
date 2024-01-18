package com.jubasbackend.service.specialty;

import com.jubasbackend.service.SpecialtyServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FindSpecialtyOnRepositoryTest extends SpecialtyServiceBaseTest {

    @Test
    @DisplayName("Deve ocorrer um erro caso a especialidade não exista.")
    void shouldThrowExceptionWhenSpecialtyDoesNotExists() {
        //ARRANGE
        var specialtyId = UUID.randomUUID();
        doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findSpecialtyOnRepository(specialtyId));

        var capturedCategoryId = uuidArgumentCaptor.getValue();

        assertEquals("Unregistered Specialty.", exception.getMessage());
        assertEquals(specialtyId, capturedCategoryId);

        verify(repository).findById(capturedCategoryId);
        verifyNoMoreInteractions(repository);
    }
}
