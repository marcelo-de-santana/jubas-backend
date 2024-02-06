package com.jubasbackend.core.user;


import com.jubasbackend.core.user.dto.UserPermissionRequest;
import com.jubasbackend.core.user.enums.PermissionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateUserTest extends UserServiceBaseTest {

    UUID userId = UUID.randomUUID();
    ;
    PermissionType currentPermission = PermissionType.CLIENTE;
    UserEntity currentUser = UserEntity.builder()
            .id(userId)
            .email("atual@email.com")
            .password("00000000")
            .permission(currentPermission)
            .build();

    @Test
    @DisplayName("Deve atualizar o usuário com sucesso.")
    void shouldUpdateUserSuccessfully() {
        //ARRANGE
        var newPermission = PermissionType.BARBEIRO;
        var request = new UserPermissionRequest("novo@email.com", "12345678", newPermission);
        var updatedUser = UserEntity.builder()
                .id(userId)
                .email(request.email())
                .password(request.password())
                .permission(newPermission)
                .build();

        doReturn(Optional.of(currentUser)).when(repository).findById(userId);
        doReturn(false).when(repository).existsByEmail(request.email());
        doReturn(updatedUser).when(repository).save(any(UserEntity.class));

        //ACT
        var response = service.updateUser(userId, request);

        //ASSERT
        assertNotNull(response);
        assertEquals(request.email(), response.email());
        assertEquals(newPermission, response.permission());
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).existsByEmail(request.email());
        verify(repository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso o e-mail já esteja em uso.")
    void shouldThrowErrorWhenEmailIsInUse() {
        //ARRANGE
        var request = new UserPermissionRequest("existente@gmail.com", "012345678", PermissionType.CLIENTE);
        doReturn(Optional.of(currentUser)).when(repository).findById(userId);
        doReturn(true).when(repository).existsByEmail(request.email());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(userId, request));
        assertEquals("E-mail is already in use.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso o ID não esteja cadastrado.")
    void shouldThrowErrorWhenUserIdIsNotRegistred() {
        //ARRANGE
        var request = new UserPermissionRequest("novo@email.com", "12345678", PermissionType.BARBEIRO);
        doReturn(Optional.empty()).when(repository).findById(any());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateUser(userId, request));
        assertEquals("User doesn't exists.", exception.getMessage());
        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository);
    }
}
