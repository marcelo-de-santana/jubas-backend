package com.jubasbackend.service.category;

import com.jubasbackend.service.CategoryServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FindCategoryOnRepositoryTest extends CategoryServiceBaseTest {

    @Test
    @DisplayName("Deve ocorrer uma exceção caso a categoria não exista.")
    void shouldThrowExceptionWhenCategoryDoesNotExists() {
        //ARRANGE
        Short categoryId = 1;
        doReturn(Optional.empty()).when(repository).findById(shortArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findCategoryOnRepository(categoryId));

        assertEquals("Category doesn't exists.", exception.getMessage());
        verify(repository).findById(shortArgumentCaptor.getValue());
        verifyNoMoreInteractions(repository);
    }
}
