package com.jubasbackend.service.profile;

import com.jubasbackend.controller.request.RecoveryPasswordRequest;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecoveryPasswordTest extends AbstractProfileServiceTest {

    final static String USER_EMAIL = "cliente@gmail.com";
    final static String PROFILE_CPF = "0123456789";
    final static String NEW_PASSWORD = "12345678910";

    RecoveryPasswordRequest request = new RecoveryPasswordRequest(USER_EMAIL, PROFILE_CPF, NEW_PASSWORD);

    User user = User.builder()
            .email(USER_EMAIL)
            .password(NEW_PASSWORD)
            .build();
    Profile profile = Profile.builder()
            .cpf(PROFILE_CPF).
            user(user)
            .build();

    @Test
    @DisplayName("Deve alterar senha com sucesso.")
    void shouldChangePasswordWithSuccess() {
        //ARRANGE
        doReturn(Optional.ofNullable(profile)).when(profileRepository).findByCpfAndUserEmail(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        doReturn(profile).when(profileRepository).save(profileArgumentCaptor.capture());

        //ACT
        var response = service.recoveryPassword(request);

        //ASSERT

        var requestsCaptured = stringArgumentCaptor.getAllValues();
        var profileCaptured = profileArgumentCaptor.getValue();

        assertNotNull(response);

        assertEquals(request.profileCpf(), requestsCaptured.get(0));
        assertEquals(request.email(), requestsCaptured.get(1));

        assertEquals(request.profileCpf(), profileCaptured.getCpf());
        assertEquals(request.email(), profileCaptured.getUser().getEmail());
        assertEquals(request.newPassword(), profileCaptured.getUser().getPassword());

        verify(profileRepository, times(1)).findByCpfAndUserEmail(requestsCaptured.get(0), requestsCaptured.get(1));
        verify(profileRepository, times(1)).save(profileCaptured);
        verifyNoMoreInteractions(profileRepository);
    }

    @Test
    @DisplayName("Deve ocorrer um erro quando o perfil não for encontrado.")
    void shouldThrowErrorWhenProfileIsNotFound() {
        //ARRANGE
        doReturn(Optional.empty()).when(profileRepository).findByCpfAndUserEmail(request.profileCpf(), request.email());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.recoveryPassword(request));
        assertEquals("No profile found for the given email and CPF combination.", exception.getMessage());

        verify(profileRepository, times(1)).findByCpfAndUserEmail(request.profileCpf(), request.email());
        verifyNoMoreInteractions(profileRepository);
    }
}
