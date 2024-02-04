package com.jubasbackend.core.user;

import com.jubasbackend.core.permission.PermissionEntity;
import com.jubasbackend.core.user.dto.UserPermissionRequest;
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
    PermissionEntity currentPermission = PermissionEntity.builder().id((short) 3).build();
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
        var newPermission = PermissionEntity.builder().id((short) 2).build();
        var request = new UserPermissionRequest("novo@email.com", "12345678", (short) 2);
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
        assertEquals(request.permissionId(), response.permission().id());
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).existsByEmail(request.email());
        verify(repository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso o e-mail já esteja em uso.")
    void shouldThrowErrorWhenEmailIsInUse() {
        //ARRANGE
        var request = new UserPermissionRequest("existente@gmail.com", "012345678", (short) 3);
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
        var request = new UserPermissionRequest("novo@email.com", "12345678", (short) 2);
        doReturn(Optional.empty()).when(repository).findById(any());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.updateUser(userId, request));
        assertEquals("User doesn't exists.", exception.getMessage());
        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository);
    }
}
