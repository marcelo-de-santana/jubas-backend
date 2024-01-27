package com.jubasbackend.core.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FindUserByIdOnRepositoryTest extends UserServiceBaseTest {
    @Test
    @DisplayName("Deve ocorrer uma exceção caso o ID não esteja cadastrado.")
    void shouldThrowErrorWhenUserIdIsNotRegistred() {
        //ARRANGE
        var idNotExists = UUID.randomUUID();
        doReturn(Optional.empty()).when(repository).findById(idNotExists);

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findUserByIdOnRepository(idNotExists));
        assertEquals("User doesn't exists.", exception.getMessage());
        verify(repository, times(1)).findById(idNotExists);
        verifyNoMoreInteractions(repository);
    }
}
