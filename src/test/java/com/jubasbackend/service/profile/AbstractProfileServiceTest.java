package com.jubasbackend.service.profile;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.service.ProfileService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractProfileServiceTest {

    @Mock
    protected ProfileRepository profileRepository;

    @Mock
    protected UserRepository userRepository;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    protected ArgumentCaptor<Profile> profileArgumentCaptor;

    @Captor
    protected ArgumentCaptor<UUID> uuidArgumentCaptor;


    @InjectMocks
    protected ProfileService service;

}
