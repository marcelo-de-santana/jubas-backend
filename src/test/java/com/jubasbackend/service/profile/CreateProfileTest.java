package com.jubasbackend.service.profile;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.controller.request.ProfileRequest;
import com.jubasbackend.exception.APIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateProfileTest extends AbstractProfileServiceTest {

    ProfileRequest request;
    Profile profile;

    @Captor
    ArgumentCaptor<Profile> profileEntityArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;


    @BeforeEach
    public void setup() {
        request = new ProfileRequest("Juninho Almeida", "12345678910", false, UUID.randomUUID());
        profile = new Profile(request);
    }

    @Test
    @DisplayName("Deve cadastrar perfil com sucesso.")
    void shouldCreateProfileWithSuccessfully() {
        //ARRANGE
        doReturn(true).when(repository).existsByUserId(uuidArgumentCaptor.capture());
        doReturn(profile).when(repository).save(profileEntityArgumentCaptor.capture());

        //ACT
        var response = service.createProfile(request);

        //ASSERT
        assertNotNull(response);

        var profileCaptured = profileEntityArgumentCaptor.getValue();

        assertEquals(request.name(), profileCaptured.getName());
        assertEquals(request.cpf(), profileCaptured.getCpf());
        assertEquals(request.statusProfile(), profileCaptured.isStatusProfile());

        verify(repository, times(1)).existsByUserId(uuidArgumentCaptor.getValue());
        verify(repository, times(1)).save(profileCaptured);
    }

    @Test
    @DisplayName("Deve lançar um erro quando o usuário não existe.")
    void shouldThrowErrorWhenUserDoesNotExists() {
        //ARRANGE
        doReturn(false).when(repository).existsByUserId(uuidArgumentCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(APIException.class,
                () -> service.createProfile(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User doesn't exists.", exception.getMessage());
        assertEquals(request.userId(), uuidArgumentCaptor.getValue());
        verify(repository).existsByUserId(uuidArgumentCaptor.getValue());
        verifyNoMoreInteractions(repository);
    }
}
