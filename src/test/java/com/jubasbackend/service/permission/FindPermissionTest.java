package com.jubasbackend.service.permission;

import com.jubasbackend.infrastructure.entity.PermissionEntity;
import com.jubasbackend.service.PermissionServiceBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class FindPermissionTest extends PermissionServiceBaseTest {
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

}
