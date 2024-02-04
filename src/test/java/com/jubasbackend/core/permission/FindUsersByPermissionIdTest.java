package com.jubasbackend.core.permission;

import com.jubasbackend.core.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindUsersByPermissionIdTest extends PermissionServiceBaseTest {
    @Test
    @DisplayName("Deve buscar usuário por nível de permissão com sucesso.")
    void shouldFindUsersByPermissionWithSuccess() {
        //ARRANGE
        Short permissionId = 3;
        var listOfUsers = Collections.singletonList(UserEntity.builder().build());
        var currentPermission = PermissionEntity.builder().id(permissionId).users(listOfUsers).build();
        doReturn(Optional.of(currentPermission)).when(repository).findById(permissionId);

        //ACT
        var response = service.findUsersByPermission(permissionId);

        //ASSERT
        assertNotNull(response);
        verify(repository, times(1)).findById(permissionId);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso a permissão não exista.")
    void shouldThrowErrorWhenPermissionDoesNotExists() {
        //ARRANGE
        Short nonExistentPermissionId = 0;
        doReturn(Optional.empty()).when(repository).findById(nonExistentPermissionId);

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findUsersByPermission(nonExistentPermissionId));

        assertEquals("Permission not found.", exception.getMessage());
        verify(repository, times(1)).findById(nonExistentPermissionId);

    }
}
