package com.jubasbackend.core.specialty;

import com.jubasbackend.core.specialty.dto.SpecialtyRequest;
import com.jubasbackend.core.category.CategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateSpecialtyTest extends SpecialtyServiceBaseTest {

    SpecialtyRequest request = new SpecialtyRequest("Nova especialidade", "00:20", 20.00F, (short) 1);

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    @DisplayName("Deve criar especialidade com sucesso.")
    void shouldCreateSpecialtyWithSuccessfully() {
        //ARRANGE
        var specialty = SpecialtyEntity.builder()
                .name(request.name())
                .timeDuration(request.timeDuration())
                .price(request.price())
                .category(CategoryEntity.builder().id(request.categoryId()).build()).build();
        doReturn(false).when(repository).existsByName(stringArgumentCaptor.capture());
        doReturn(specialty).when(repository).save(specialtyEntityArgumentCaptor.capture());

        //ACT
        var response = service.createSpecialty(request);

        //ASSERT
        var capturedName = stringArgumentCaptor.getValue();
        var capturedSpecialty = specialtyEntityArgumentCaptor.getValue();

        assertNotNull(response);
        assertEquals(request.name(), capturedName);
        assertEquals(request.name(), capturedSpecialty.getName());
        assertEquals(request.price(), capturedSpecialty.getPrice());
        assertEquals(request.timeDuration(), capturedSpecialty.getTimeDuration());
        assertEquals(request.categoryId(), capturedSpecialty.getCategory().getId());

        verify(repository, times(1)).existsByName(capturedName);
        verify(repository, times(1)).save(capturedSpecialty);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro caso a especialidade já exista.")
    void shouldThrowExceptionWhenSpecialtyAlreadyExists() {
        //ARRANGE
        var specialty = SpecialtyEntity.builder()
                .name(request.name())
                .timeDuration(request.timeDuration())
                .price(request.price())
                .category(CategoryEntity.builder().id(request.categoryId()).build()).build();
        doReturn(true).when(repository).existsByName(stringArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createSpecialty(request));

        var capturedName = stringArgumentCaptor.getValue();

        assertEquals("Specialty already registered.", exception.getMessage());
        assertEquals(request.name(), capturedName);

        verify(repository, times(1)).existsByName(capturedName);
        verifyNoMoreInteractions(repository);
    }
}