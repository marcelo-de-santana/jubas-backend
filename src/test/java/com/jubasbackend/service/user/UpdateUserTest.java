package com.jubasbackend.service.user;

import com.jubasbackend.api.dto.request.UserRequest;
import com.jubasbackend.infrastructure.entity.PermissionEntity;
import com.jubasbackend.infrastructure.entity.UserEntity;
import com.jubasbackend.service.UserServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateUserTest extends UserServiceBaseTest {

    UUID userId;
    PermissionEntity currentPermission;
    UserEntity currentUser;

    @BeforeEach
    void setup() {
        this.userId = UUID.randomUUID();
        this.currentPermission = PermissionEntity.builder().id((short) 3).build();
        this.currentUser = UserEntity.builder()
                .id(userId)
                .email("atual@email.com")
                .password("00000000")
                .permission(currentPermission)
                .build();
    }

    @Test
    @DisplayName("Deve atualizar o usuário com sucesso.")
    void shouldUpdateUserSuccessfully() {
        //ARRANGE
        var newPermission = PermissionEntity.builder().id((short) 2).build();
        var request = new UserRequest("novo@email.com", "12345678", (short) 2);
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
        var request = new UserRequest("existente@gmail.com", "012345678", (short) 3);
        doReturn(Optional.of(currentUser)).when(repository).findById(userId);
        doReturn(true).when(repository).existsByEmail(request.email());

        //ACT & ASSERT
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(userId, request));
        assertEquals("E-mail is already in use.", exception.getMessage());
    }
}
