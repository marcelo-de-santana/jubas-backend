package com.jubasbackend.service.category;

import com.jubasbackend.domain.entity.CategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateCategoryTest extends AbstractCategoryServiceTest {
    @Test
    @DisplayName("Deve atualizar categoria com sucesso.")
    void shouldUpdateCategoryWithSuccess() {
        //ARRANGE
        Short categoryId = 1;
        var categoryName = "Novo nome";
        var currentCategory = CategoryEntity.builder().id(categoryId).name("Antigo nome").build();
        var updatedCategory = CategoryEntity.builder().id(categoryId).name(categoryName).build();

        doReturn(Optional.of(currentCategory)).when(repository).findById(shortArgumentCaptor.capture());
        doReturn(updatedCategory).when(repository).save(categoryEntityArgumentCaptor.capture());

        //ACT
        service.updateCategory(categoryId, categoryName);

        //ASSERT
        var capturedId = shortArgumentCaptor.getValue();
        var categoryCaptured = categoryEntityArgumentCaptor.getValue();

        assertEquals(categoryId, capturedId);
        assertEquals(categoryName, categoryCaptured.getName());

        verify(repository, times(1)).findById(capturedId);
        verify(repository, times(1)).save(categoryCaptured);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso a categoria não exista.")
    void shouldThrowExceptionWhenCategoryDoesNotExists() {
        //ARRANGE
        Short categoryId = 1;
        doReturn(Optional.empty()).when(repository).findById(shortArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateCategory(categoryId, "Novo Nome"));

        var capturedId = shortArgumentCaptor.getValue();

        assertEquals("Category doesn't exists.", exception.getMessage());
        assertEquals(categoryId, categoryId);

        verify(repository).findById(shortArgumentCaptor.getValue());
        verifyNoMoreInteractions(repository);
    }
}
