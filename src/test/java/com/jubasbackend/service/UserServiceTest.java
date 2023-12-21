package com.jubasbackend.service;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.dto.request.UserMinimalRequest;
import com.jubasbackend.dto.request.UserRequest;
import com.jubasbackend.dto.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("Verifica se login é realizado com sucesso.")
    void findUserAccountCase1() {
        var request = new UserMinimalRequest("cliente@gmail.com", "12345678");
        var user = new User().builder()
                .email("cliente@gmail.com")
                .password("12345678")
                .userPermission(new UserPermission((short) 3)).build();
        when(repository.findByEmail(request.email())).thenReturn(Optional.ofNullable(user));

        var result = service.findUserAccount(request);

        assertNotNull(result);
        assertEquals(new UserResponse(user), result);
        verify(repository, times(1)).findByEmail(request.email());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Verifica se ocorre um erro ao passar um e-mail não cadastrado.")
    void findUserAccountCase2() {
        var request = new UserMinimalRequest("naoexiste@teste.com", "12345678");
        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> {
            service.findUserAccount(request);
        });

        assertEquals("The provided email is not registered in our system.", exception.getMessage());
        verify(repository, times(1)).findByEmail(request.email());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Verifica se ocorre um erro ao passar a senha errada.")
    void findUserAccountCase3() {
        var request = new UserMinimalRequest("cliente@gmail.com", "00000000");
        var user = new User().builder()
                .email("cliente@gmail.com")
                .password("12345678")
                .userPermission(new UserPermission((short) 3)).build();
        when(repository.findByEmail(request.email())).thenReturn(Optional.ofNullable(user));

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findUserAccount(request);
        });

        assertEquals("Incorrect Email or Password.", exception.getMessage());
        verify(repository, times(1)).findByEmail(request.email());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Verifica se usuário é criado com sucesso.")
    void createUserCase1() {
        var request = new UserRequest("novoCliente@teste.com", "12345678", (short) 3);
        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.save(Mockito.any(User.class))).thenReturn(new User(request));

        var userResponse = service.create(request);

        assertNotNull(userResponse);
        assertEquals(request.email(), userResponse.email());
        assertEquals(request.userPermissionId(), userResponse.userPermission().getId());
        verify(repository, times(1)).existsByEmail(request.email());
        verify(repository, times(1)).save(Mockito.any(User.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Verifica se ocorre um erro ao tentar cadastrar um usuário existente.")
    void createUserCase2() {
        var request = new UserRequest("cliente@gmail.com", "12345678", (short) 3);
        when(repository.existsByEmail(request.email())).thenReturn(true);

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(request);
        });

        assertEquals("User already exists.", exception.getMessage());
        verify(repository, times(1)).existsByEmail(request.email());
        verifyNoMoreInteractions(repository);
    }
}
