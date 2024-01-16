package com.jubasbackend.service.user;

import com.jubasbackend.api.dto.request.UserRequest;
import com.jubasbackend.infrastructure.entity.UserEntity;
import com.jubasbackend.service.UserServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserTest extends UserServiceBaseTest {
    UserRequest request;

    @BeforeEach
    void setup() {
        this.request = new UserRequest("novoCliente@teste.com", "12345678", (short) 3);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso.")
    void shouldCreateUserWithSuccessfully() {
        //ARRANGE
        var newUser = new UserEntity(request);
        doReturn(false).when(repository).existsByEmail(request.email());
        doReturn(newUser).when(repository).save(any(UserEntity.class));

        //ACT
        var response = service.createUser(request);

        //ASSERT
        assertNotNull(response);
        assertEquals(request.email(), response.email());
        assertEquals(request.permissionId(), response.permission().id());
        verify(repository, times(1)).existsByEmail(request.email());
        verify(repository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso o e-mail já esteja cadastrado.")
    void AnExceptionMustOccurWhenTheEmailIsAlreadyRegistered() {
        //ARRANGE
        doReturn(true).when(repository).existsByEmail(request.email());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            service.createUser(request);
        });
        assertEquals("User already exists.", exception.getMessage());
    }
}