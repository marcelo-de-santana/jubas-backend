package com.jubasbackend.service.specialty;

import com.jubasbackend.controller.request.SpecialtyRequest;
import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.entity.Specialty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UpdateSpecialtyTest extends AbstractSpecialtyServiceTest {

    SpecialtyRequest request = new SpecialtyRequest("Novo nome", LocalTime.parse("00:20"), BigDecimal.valueOf(20.00), (short) 1);
    UUID specialtyId = UUID.randomUUID();

    @Test
    @DisplayName("Deve atualizar a especialidade com sucesso.")
    void shouldUpdateSpecialtyWithSuccessfully() {
        //ARRANGE
        var currentSpecialty = Specialty.builder()
                .name("Antigo nome")
                .timeDuration(LocalTime.parse("00:10"))
                .price(BigDecimal.valueOf(10.00))
                .category(Category.builder().id((short) 2).build()).build();

        var updatedSpecialty = Specialty.builder()
                .name(request.name())
                .timeDuration(request.timeDuration())
                .price(request.price())
                .category(Category.builder().id(request.categoryId()).build()).build();
        doReturn(Optional.of(currentSpecialty)).when(repository).findById(uuidArgumentCaptor.capture());
        doReturn(updatedSpecialty).when(repository).save(specialtyEntityArgumentCaptor.capture());

        //ACT
        service.updateSpecialty(specialtyId, request);

        // ASSERT
        var capturedSpecialtyId = uuidArgumentCaptor.getValue();
        var capturedSpecialty = specialtyEntityArgumentCaptor.getValue();

        assertEquals(specialtyId, capturedSpecialtyId);
        assertEquals(request.name(), capturedSpecialty.getName());
        assertEquals(request.price(), capturedSpecialty.getPrice());
        assertEquals(request.timeDuration(), capturedSpecialty.getTimeDuration());
        assertEquals(request.categoryId(), capturedSpecialty.getCategory().getId());

        verify(repository, times(1)).findById(capturedSpecialtyId);
        verify(repository, times(1)).save(capturedSpecialty);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro caso a especialidade não exista.")
    void shouldThrowExceptionWhenSpecialtyDoesNotExists() {
        //ARRANGE
        doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateSpecialty(specialtyId, request));

        var capturedCategoryId = uuidArgumentCaptor.getValue();

        assertEquals("Unregistered Specialty.", exception.getMessage());
        assertEquals(specialtyId, capturedCategoryId);

        verify(repository).findById(capturedCategoryId);
        verifyNoMoreInteractions(repository);
    }


}
