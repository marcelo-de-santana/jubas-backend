package com.jubasbackend.service.profile;

import com.jubasbackend.controller.request.ProfileRequest;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.exception.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateProfileTest extends AbstractProfileServiceTest {

    final static String PROFILE_NAME = "Juninho Almeida";
    final static String PROFILE_CPF = "12345678910";
    final static boolean PROFILE_STATUS = true;
    final static UUID USER_ID = UUID.randomUUID();

    ProfileRequest request = new ProfileRequest(PROFILE_NAME, PROFILE_CPF, PROFILE_STATUS, USER_ID);
    Profile profile = new Profile(request);

    @Test
    @DisplayName("Deve cadastrar perfil com sucesso.")
    void shouldCreateProfileWithSuccessfully() {
        //ARRANGE
        doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
        doReturn(profile).when(profileRepository).save(profileArgumentCaptor.capture());

        //ACT
        var response = service.createProfile(request);

        //ASSERT
        assertNotNull(response);

        var capturedUserId = uuidArgumentCaptor.getValue();
        var capturedProfile = profileArgumentCaptor.getValue();

        assertEquals(USER_ID, capturedUserId);
        assertEquals(PROFILE_NAME, capturedProfile.getName());
        assertEquals(PROFILE_CPF, capturedProfile.getCpf());
        assertEquals(PROFILE_STATUS, capturedProfile.isStatusProfile());

        verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
        verify(profileRepository, times(1)).save(capturedProfile);
        verifyNoMoreInteractions(userRepository, profileRepository);
    }

    @Test
    @DisplayName("Deve lançar um erro quando o usuário não existe.")
    void shouldThrowErrorWhenUserDoesNotExists() {
        //ARRANGE
        doReturn(false).when(userRepository).existsById(any());

        //ACT & ASSERT
        var exception = assertThrows(APIException.class,
                () -> service.createProfile(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User doesn't exists.", exception.getMessage());
        verifyNoMoreInteractions(userRepository, profileRepository);
    }
}
