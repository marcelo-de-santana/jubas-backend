package com.jubasbackend.core.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DeleteCategoryTest extends CategoryServiceBaseTest {

    @Test
    @DisplayName("Deve excluir categoria com sucesso.")
    void shouldDeleteCategorySuccessfully() {
        //ARRANGE
        Short categoryId = 1;
        var categoryToDelete = CategoryEntity.builder().build();

        doNothing().when(repository).deleteById(shortArgumentCaptor.capture());

        //ACT
        service.deleteCategory(categoryId);

        //ASSERT
        var capturedId = shortArgumentCaptor.getValue();

        assertEquals(categoryId, capturedId);

        verify(repository, times(1)).deleteById(capturedId);
        verifyNoMoreInteractions(repository);
    }
}
