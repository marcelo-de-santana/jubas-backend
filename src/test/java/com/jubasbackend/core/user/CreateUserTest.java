package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserPermissionRequest;
import com.jubasbackend.core.user.enums.PermissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserTest extends UserServiceBaseTest {
    UserPermissionRequest request;

    @BeforeEach
    void setup() {
        this.request = new UserPermissionRequest("novoCliente@teste.com", "12345678", PermissionType.CLIENTE);
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
        assertEquals(request.permission(), response.permission());
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
