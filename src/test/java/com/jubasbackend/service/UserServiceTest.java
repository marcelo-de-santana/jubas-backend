package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.UserMinimalRequest;
import com.jubasbackend.api.dto.request.UserRequest;
import com.jubasbackend.domain.entity.PermissionEntity;
import com.jubasbackend.domain.entity.UserEntity;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl service;

    @Nested
    class FindUserByIdOnRepository {

        @Test
        @DisplayName("Deve ocorrer uma exceção caso o ID não esteja cadastrado.")
        void shouldThrowErrorWhenUserIdIsNotRegistred() {
            //ARRANGE
            var idNotExists = UUID.randomUUID();
            doReturn(Optional.empty()).when(repository).findById(idNotExists);

            //ACT & ASSERT
            var exception = assertThrows(NoSuchElementException.class,
                    () -> service.findUserByIdOnRepository(idNotExists));
            assertEquals("User doesn't exists.", exception.getMessage());
            verify(repository, times(1)).findById(idNotExists);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    class FindUserByEmailOnRepository {
        @Test
        @DisplayName("Deve ocorrer uma exceção caso o e-mail não esteja cadastrado.")
        void shouldThrowErrorWhenEmailNotRegistered() {
            //ARRANGE
            var email = "naocadastrado@teste.com";
            doReturn(Optional.empty()).when(repository).findByEmail(email);

            //ACT & ASSERT
            var exception = assertThrows(NoSuchElementException.class, () -> {
                service.findUserByEmailOnRepository(email);
            });
            assertEquals("The provided email is not registered in our system.", exception.getMessage());
        }
    }

    @Nested
    class AuthenticateUser {

        UserMinimalRequest request;

        @BeforeEach
        void setup() {
            this.request = new UserMinimalRequest("cliente@gmail.com", "12345678");
        }

        @Test
        @DisplayName("Deve autenticar o usuário com sucesso.")
        void shouldAuthenticateUserSuccessfully() {
            //ARRANGE
            var permission = PermissionEntity.builder().id((short) 3).build();
            var user = UserEntity.builder().email("cliente@gmail.com").password("12345678").permission(permission).build();
            doReturn(Optional.of(user)).when(repository).findByEmail(request.email());

            //ACT
            var response = service.findUserAccount(request);

            //ASSERT
            assertNotNull(response);
            assertEquals(request.email(), response.email());
            assertNotNull(response.permission().id());
            verify(repository,times(1)).findByEmail(request.email());
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("Deve ocorrer uma exceção caso a senha esteja incorreta.")
        void shouldThrowErrorWhenIncorrectPassword() {
            //ARRANGE
            var permission = PermissionEntity.builder().id((short) 3).build();
            var user = UserEntity.builder().email("cliente@gmail.com").password("12345678910").permission(permission).build();
            doReturn(Optional.ofNullable(user)).when(repository).findByEmail(request.email());

            //ACT & ASSERT
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                service.findUserAccount(request);
            });
            assertEquals("Incorrect Email or Password.", exception.getMessage());
        }
    }

    @Nested
    class CreateUser {

        UserRequest request;

        @BeforeEach
        void setup(){
            this.request = new UserRequest("novoCliente@teste.com", "12345678", (short) 3);
        }

        @Test
        @DisplayName("Deve criar usuário com sucesso.")
        void shouldCreateUserWithSuccessfully() {
            //ARRANGE
            var newUser = new UserEntity(request);
            doReturn(false).when(repository).existsByEmail(request.email());
            doReturn(newUser).when(repository).save(any(UserEntity.class));

            //ACT
            var response = service.createUser(request);

            //ASSERT
            assertNotNull(response);
            assertEquals(request.email(), response.email());
            assertEquals(request.permissionId(), response.permission().id());
            verify(repository, times(1)).existsByEmail(request.email());
            verify(repository, times(1)).save(any(UserEntity.class));
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("Deve ocorrer uma exceção caso o e-mail já esteja cadastrado.")
        void AnExceptionMustOccurWhenTheEmailIsAlreadyRegistered() {
            //ARRANGE
            doReturn(true).when(repository).existsByEmail(request.email());

            //ACT & ASSERT
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                service.createUser(request);
            });
            assertEquals("User already exists.", exception.getMessage());
        }
    }

    @Nested
    class UpdateUser {
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
}
