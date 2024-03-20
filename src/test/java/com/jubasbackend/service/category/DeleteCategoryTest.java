package com.jubasbackend.service.category;

import com.jubasbackend.domain.entity.CategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DeleteCategoryTest extends AbstractCategoryServiceTest {

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
