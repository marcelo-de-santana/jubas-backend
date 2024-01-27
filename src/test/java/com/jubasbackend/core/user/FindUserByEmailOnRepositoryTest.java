package com.jubasbackend.core.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

class FindUserByEmailOnRepositoryTest extends UserServiceBaseTest {
    @Test
    @DisplayName("Deve ocorrer uma exceção caso o e-mail não esteja cadastrado.")
    void shouldThrowErrorWhenEmailNotRegistered() {
        //ARRANGE
        var email = "naocadastrado@teste.com";
        doReturn(Optional.empty()).when(repository).findByEmail(email);

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class, () -> {
            service.findUserByEmailOnRepository(email);
        });
        assertEquals("The provided email is not registered in our system.", exception.getMessage());
    }
}
