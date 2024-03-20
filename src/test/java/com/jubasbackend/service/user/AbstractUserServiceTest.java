package com.jubasbackend.service.user;

import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractUserServiceTest {

    @Mock
    protected UserRepository repository;

    @InjectMocks
    protected UserService service;
}
