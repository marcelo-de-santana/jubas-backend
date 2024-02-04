package com.jubasbackend.core.permission;

import com.jubasbackend.core.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindPermissionsAndUsersTest extends PermissionServiceBaseTest {
    @Test
    @DisplayName("Deve buscar todos as permissões e usuários com sucesso.")
    void shouldFindPermissionsAndUserWithSuccess() {
        //ARRANGE
        var users = Collections.singletonList(UserEntity.builder().build());
        var permissions = Collections.singletonList(PermissionEntity.builder()
                .id((short) 1).type("ADMIN").users(users).build());
        doReturn(permissions).when(repository).findAll();

        //ACT & ASSERT
        assertNotNull(service.findPermissionsAndUsers());
        verify(repository, times(1)).findAll();
    }

}
