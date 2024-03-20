package com.jubasbackend.service.profile;

import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.controller.request.ProfileUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateProfileTest extends AbstractProfileServiceTest {

    ProfileUserRequest request;
    ProfileEntity profile;

    @Captor
    ArgumentCaptor<ProfileEntity> profileEntityArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;


    @BeforeEach
    public void setup() {
        request = new ProfileUserRequest("Juninho Almeida", "12345678910", false, UUID.randomUUID());
        profile = new ProfileEntity(request);
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
        var exception = assertThrows(IllegalArgumentException.class,
                () -> service.createProfile(request));

        assertEquals("User doesn't exists.", exception.getMessage());
        assertEquals(request.userId(), uuidArgumentCaptor.getValue());
        verify(repository).existsByUserId(uuidArgumentCaptor.getValue());
        verifyNoMoreInteractions(repository);
    }
}
