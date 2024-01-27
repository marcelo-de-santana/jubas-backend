package com.jubasbackend.core.profile.service;

import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.profile.dto.ProfileRecoveryRequest;
import com.jubasbackend.core.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecoveryPasswordTest extends ProfileServiceBaseTest {

    ProfileRecoveryRequest request;
    ProfileEntity profile;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    ArgumentCaptor<ProfileEntity> profileEntityArgumentCaptor;

    @BeforeEach
    public void setup() {
        this.request = new ProfileRecoveryRequest("cliente@gmail.com", "0123456789", "12345678910");
        var user = UserEntity.builder().email(request.email()).password(request.newPassword()).build();
        this.profile = ProfileEntity.builder().cpf(request.profileCpf()).user(user).build();
    }

    @Test
    @DisplayName("Deve alterar senha com sucesso.")
    void shouldChangePasswordWithSuccess() {
        //ARRANGE
        doReturn(Optional.ofNullable(profile)).when(repository).findByCpfAndUserEmail(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        doReturn(profile).when(repository).save(profileEntityArgumentCaptor.capture());

        //ACT
        var response = service.recoveryPassword(request);

        //ASSERT

        var requestsCaptured = stringArgumentCaptor.getAllValues();
        var profileCaptured = profileEntityArgumentCaptor.getValue();

        assertNotNull(response);

        assertEquals(request.profileCpf(), requestsCaptured.get(0));
        assertEquals(request.email(), requestsCaptured.get(1));

        assertEquals(request.profileCpf(), profileCaptured.getCpf());
        assertEquals(request.email(), profileCaptured.getUser().getEmail());
        assertEquals(request.newPassword(), profileCaptured.getUser().getPassword());

        verify(repository, times(1)).findByCpfAndUserEmail(requestsCaptured.get(0), requestsCaptured.get(1));
        verify(repository, times(1)).save(profileCaptured);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro quando o perfil não for encontrado.")
    void shouldThrowErrorWhenProfileIsNotFound() {
        //ARRANGE
        doReturn(Optional.empty()).when(repository).findByCpfAndUserEmail(request.profileCpf(), request.email());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.recoveryPassword(request));
        assertEquals("No profile found for the given email and CPF combination.", exception.getMessage());

        verify(repository, times(1)).findByCpfAndUserEmail(request.profileCpf(), request.email());
        verifyNoMoreInteractions(repository);
    }
}
