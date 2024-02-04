package com.jubasbackend.core.profile;

import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.profile.ProfileServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class ProfileServiceBaseTest {

    @Mock
    protected ProfileRepository repository;

    @InjectMocks
    protected ProfileServiceImpl service;

}
