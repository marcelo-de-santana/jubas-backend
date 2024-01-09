package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.dto.request.ProfileRecoveryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    ProfileRepository repository;

    @Mock
    UserService userService;

    @InjectMocks
    ProfileService profileService;

    Profile profile;

    @BeforeEach
    public void setup() {
        var user = User.builder().email("cliente@gmail.com").password("12345678").build();
        profile = Profile.builder().name("cliente@gmail.com").cpf("12345678910").statusProfile(true).user(user).build();
    }

    @Test
    @DisplayName("Verifica a recuperação de senha com sucesso.")
    void recoveryPasswordCase1() {
        var request = new ProfileRecoveryRequest("cliente@gmail.com", "0123456789", "12345678910");
        when(repository.findByCpfAndUserEmail(request.profileCpf(), request.email())).thenReturn(Optional.ofNullable(profile));
        when(repository.save(isA(Profile.class))).thenReturn(profile);

        var response = profileService.recoveryPassword(request);

        assertNotNull(response);
        verify(repository, times(1)).findByCpfAndUserEmail(request.profileCpf(), request.email());
        verify(repository, times(1)).save(isA(Profile.class));
        verifyNoMoreInteractions(repository);
    }

}