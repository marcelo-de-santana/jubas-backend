package com.jubasbackend.service;

import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.entity.UserEntity;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.api.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.service.impl.ProfileServiceImpl;
import com.jubasbackend.service.impl.UserServiceImpl;
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
    UserServiceImpl userService;

    @InjectMocks
    ProfileServiceImpl profileService;

    ProfileEntity profile;

    @BeforeEach
    public void setup() {
        var user = UserEntity.builder().email("cliente@gmail.com").password("12345678").build();
        profile = ProfileEntity.builder().name("cliente@gmail.com").cpf("12345678910").statusProfile(true).user(user).build();
    }

    @Test
    @DisplayName("Verifica a recuperação de senha com sucesso.")
    void recoveryPasswordCase1() {
        var request = new ProfileRecoveryRequest("cliente@gmail.com", "0123456789", "12345678910");
        when(repository.findByCpfAndUserEmail(request.profileCpf(), request.email())).thenReturn(Optional.ofNullable(profile));
        when(repository.save(isA(ProfileEntity.class))).thenReturn(profile);

        var response = profileService.recoveryPassword(request);

        assertNotNull(response);
        verify(repository, times(1)).findByCpfAndUserEmail(request.profileCpf(), request.email());
        verify(repository, times(1)).save(isA(ProfileEntity.class));
        verifyNoMoreInteractions(repository);
    }

}
