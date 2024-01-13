package com.jubasbackend.service;

import com.jubasbackend.infrastructure.entity.PermissionEntity;
import com.jubasbackend.infrastructure.entity.UserEntity;
import com.jubasbackend.infrastructure.repository.PermissionRepository;
import com.jubasbackend.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
    @Mock
    PermissionRepository repository;

    @InjectMocks
    PermissionServiceImpl service;

    @Nested
    class FindPermissionOnRepository {
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

    @Nested
    class FindPermissionsOnRepository {
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

    @Nested
    class FindUsersByPermissionId {

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
    }

    @Nested
    class FindPermissions {

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

    @Nested
    class FindPermissionsAndUsers {

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

//    @Nested
//    class FindProfilesByPermissionId{
//        @Test
//        @DisplayName("")
//        void
//    }
}
