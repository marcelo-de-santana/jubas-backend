package com.jubasbackend.service.category;

import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.service.CategoryServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UptadeCategoryTest extends CategoryServiceBaseTest {

    @Test
    @DisplayName("Deve atualizar categoria com sucesso.")
    void shouldUpdateCategoryWithSuccess() {
        //ARRANGE
        Short categoryId = 1;
        var request = new CategoryRequest("Novo nome");
        var currentCategory = CategoryEntity.builder().id(categoryId).name("Antigo nome").build();
        var updatedCategory = CategoryEntity.builder().id(categoryId).name(request.name()).build();

        doReturn(Optional.of(currentCategory)).when(repository).findById(shortArgumentCaptor.capture());
        doReturn(updatedCategory).when(repository).save(categoryEntityArgumentCaptor.capture());

        //ACT
        service.updateCategory(categoryId, request);

        //ASSERT
        var capturedId = shortArgumentCaptor.getValue();
        var categoryCaptured = categoryEntityArgumentCaptor.getValue();

        assertEquals(categoryId, capturedId);
        assertEquals(request.name(), categoryCaptured.getName());

        verify(repository, times(1)).findById(capturedId);
        verify(repository, times(1)).save(categoryCaptured);
        verifyNoMoreInteractions(repository);
    }
}
