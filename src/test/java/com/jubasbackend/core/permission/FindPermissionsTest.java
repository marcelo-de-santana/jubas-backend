package com.jubasbackend.core.permission;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindPermissionsTest extends PermissionServiceBaseTest {
    @Test
    @DisplayName("Deve buscar todas as permissões com sucesso.")
    void shouldFindPermissionsWithSuccess() {
        //ARRANGE
        var permissions = Collections.singletonList(PermissionEntity.builder().build());
        doReturn(permissions).when(repository).findAll();

        //ACT & ASSERT
        assertNotNull(service.findPermissions());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção caso a lista de permissões esteja vazia.")
    void shouldThrowExceptionWhenListOfPermissionIsEmpty() {
        //ARRANGE
        var emptyList = Collections.emptyList();
        doReturn(emptyList).when(repository).findAll();

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findPermissions());
        assertEquals("Couldn't find any permissions.", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

}
