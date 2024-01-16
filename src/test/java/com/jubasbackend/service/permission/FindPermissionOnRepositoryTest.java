package com.jubasbackend.service.permission;

import com.jubasbackend.service.PermissionServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FindPermissionOnRepositoryTest extends PermissionServiceBaseTest {
    @Test
    @DisplayName("Deve ocorrer uma exceção caso a permissão não exista.")
    void shouldThrowErrorWhenPermissionDoesNotExists() {
        //ARRANGE
        Short nonExistentPermissionId = 0;
        doReturn(Optional.empty()).when(repository).findById(nonExistentPermissionId);

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findPermissionOnRepository(nonExistentPermissionId));

        assertEquals("Permission not found.", exception.getMessage());
        verify(repository, times(1)).findById(nonExistentPermissionId);

    }
}
