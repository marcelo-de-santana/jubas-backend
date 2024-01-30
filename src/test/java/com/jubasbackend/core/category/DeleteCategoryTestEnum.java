package com.jubasbackend.core.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DeleteCategoryTestEnum extends CategoryEnumServiceBaseTest {

    @Test
    @DisplayName("Deve excluir categoria com sucesso.")
    void shouldDeleteCategorySuccessfully() {
        //ARRANGE
        Short categoryId = 1;
        var categoryToDelete = CategoryEntity.builder().build();

        doReturn(Optional.of(categoryToDelete)).when(repository).findById(shortArgumentCaptor.capture());
        doNothing().when(repository).delete(categoryEntityArgumentCaptor.capture());

        //ACT
        service.deleteCategory(categoryId);

        //ASSERT
        var capturedId = shortArgumentCaptor.getValue();
        var capturedCategory = categoryEntityArgumentCaptor.getValue();

        assertEquals(categoryId, capturedId);
        assertEquals(categoryToDelete, capturedCategory);

        verify(repository, times(1)).findById(capturedId);
        verify(repository, times(1)).delete(capturedCategory);
        verifyNoMoreInteractions(repository);
    }
}
