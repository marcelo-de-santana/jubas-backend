package com.jubasbackend.service;

import com.jubasbackend.infrastructure.repository.UserRepository;
import com.jubasbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class UserServiceBaseTest {

    @Mock
    protected UserRepository repository;

    @InjectMocks
    protected UserServiceImpl service;
}
