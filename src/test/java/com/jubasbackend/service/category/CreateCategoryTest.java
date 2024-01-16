package com.jubasbackend.service.category;

import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.service.CategoryServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreateCategoryTest extends CategoryServiceBaseTest {

    CategoryRequest request;

    @BeforeEach()
    void setup() {
        this.request = new CategoryRequest("Cabelo de cabelo masculino");
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção ao tentar cadastrar uma categoria existente.")
    void shouldThrowExceptionWhenCategoryExists() {
        //ARRANGE
        doReturn(true).when(repository).existsByName(stringArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createCategory(request));

        assertEquals("Category already exists.", exception.getMessage());
        verify(repository, times(1)).existsByName(stringArgumentCaptor.getValue());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve criar categoria com sucesso.")
    void shouldCreateCategoryWithSuccessfully() {
        var category = CategoryEntity.builder().id((short) 1).name(request.name()).build();

        //ARRANGE
        var request = new CategoryRequest("Cabelo de cabelo masculino");
        doReturn(false).when(repository).existsByName(stringArgumentCaptor.capture());
        doReturn(category).when(repository).save(categoryEntityArgumentCaptor.capture());

        //ACT
        service.createCategory(request);

        //ASSERT
        var categoryNameCaptured = stringArgumentCaptor.getValue();
        var categoryCaptured = categoryEntityArgumentCaptor.getValue();

        assertEquals(request.name(), categoryNameCaptured);
        verify(repository, times(1)).existsByName(categoryNameCaptured);
        verify(repository, times(1)).save(categoryCaptured);
        verifyNoMoreInteractions(repository);
    }
}
