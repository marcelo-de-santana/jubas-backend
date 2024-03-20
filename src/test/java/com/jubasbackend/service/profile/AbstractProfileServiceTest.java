package com.jubasbackend.service.profile;

import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.service.ProfileService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractProfileServiceTest {

    @Mock
    protected ProfileRepository repository;

    @InjectMocks
    protected ProfileService service;

}
