package com.jubasbackend.service.permission;

import com.jubasbackend.service.PermissionServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FindPermissionsOnRepositoryTest extends PermissionServiceBaseTest {
    @Test
    @DisplayName("Deve ocorrer uma exceção caso a lista de permissões esteja vazia.")
    void shouldThrowExceptionWhenListOfPermissionIsEmpty() {
        //ARRANGE
        var emptyList = Collections.emptyList();
        doReturn(emptyList).when(repository).findAll();

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.findPermissionsOnRepository());
        assertEquals("Couldn't find any permissions.", exception.getMessage());
        verify(repository, times(1)).findAll();
    }
}
