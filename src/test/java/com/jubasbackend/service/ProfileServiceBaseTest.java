package com.jubasbackend.service;

import com.jubasbackend.infrastructure.repository.ProfileRepository;
import com.jubasbackend.service.impl.ProfileServiceImpl;
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
