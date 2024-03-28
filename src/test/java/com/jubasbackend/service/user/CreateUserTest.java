package com.jubasbackend.service.user;

import com.jubasbackend.controller.request.UserRequest;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.enums.PermissionType;
import com.jubasbackend.exception.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CONFLICT;

class CreateUserTest extends AbstractUserServiceTest {
    UserRequest request = new UserRequest("novoCliente@teste.com", "12345678", PermissionType.BARBEIRO);

    @Test
    @DisplayName("Deve criar usuário com sucesso.")
    void shouldCreateUserWithSuccessfully() {
        //ARRANGE
        doReturn(false).when(repository).existsByEmail(request.email());
        doReturn(new User()).when(repository).save(any());

        //ACT
        var response = service.createUser(request, token);

        //ASSERT
        assertNotNull(response);
        verify(repository, times(1)).existsByEmail(request.email());
        verify(repository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso o e-mail já esteja cadastrado.")
    void AnExceptionMustOccurWhenTheEmailIsAlreadyRegistered() {
        //ARRANGE
        doReturn(true).when(repository).existsByEmail(request.email());

        //ACT & ASSERT
        var exception = assertThrows(APIException.class, () -> {
            service.createUser(request, token);
        });

        assertEquals(CONFLICT, exception.getStatus());
        assertEquals("User already exists.", exception.getMessage());
    }
}
