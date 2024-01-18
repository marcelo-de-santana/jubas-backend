package com.jubasbackend.service.specialty;

import com.jubasbackend.api.dto.request.SpecialtyRequest;
import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.service.SpecialtyServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UpdateSpecialtyTest extends SpecialtyServiceBaseTest {

    SpecialtyRequest request = new SpecialtyRequest("Novo nome", "00:20", 20.00F, (short) 1);
    UUID specialtyId = UUID.randomUUID();

    @Test
    @DisplayName("Deve atualizar a especialidade com sucesso.")
    void shouldUpdateSpecialtyWithSuccessfully() {
        //ARRANGE
        var currentSpecialty = SpecialtyEntity.builder()
                .name("Antigo nome")
                .timeDuration(LocalTime.parse("00:10"))
                .price(10.00F)
                .category(CategoryEntity.builder().id((short) 2).build()).build();

        var updatedSpecialty = SpecialtyEntity.builder()
                .name(request.name())
                .timeDuration(request.timeDuration())
                .price(request.price())
                .category(CategoryEntity.builder().id(request.categoryId()).build()).build();
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



}
