package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.permission.PermissionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticateUserTest extends UserServiceBaseTest {
    UserRequest request;

    @BeforeEach
    void setup() {
        this.request = new UserRequest("cliente@gmail.com", "12345678");
    }

    @Test
    @DisplayName("Deve autenticar o usuário com sucesso.")
    void shouldAuthenticateUserSuccessfully() {
        //ARRANGE
        var permission = PermissionEntity.builder().id((short) 3).build();
        var user = UserEntity.builder().email("cliente@gmail.com").password("12345678").permission(permission).build();
        doReturn(Optional.of(user)).when(repository).findByEmail(request.email());

        //ACT
        var response = service.findUserAccount(request);

        //ASSERT
        assertNotNull(response);
        assertEquals(request.email(), response.email());
        assertNotNull(response.permission().id());
        verify(repository, times(1)).findByEmail(request.email());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso a senha esteja incorreta.")
    void shouldThrowErrorWhenIncorrectPassword() {
        //ARRANGE
        var permission = PermissionEntity.builder().id((short) 3).build();
        var user = UserEntity.builder().email("cliente@gmail.com").password("12345678910").permission(permission).build();
        doReturn(Optional.ofNullable(user)).when(repository).findByEmail(request.email());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findUserAccount(request);
        });
        assertEquals("Incorrect Email or Password.", exception.getMessage());
    }
}
