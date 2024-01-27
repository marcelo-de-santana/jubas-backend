package com.jubasbackend.core.user;

import com.jubasbackend.core.user.UserRepository;
import com.jubasbackend.core.user.UserServiceImpl;
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
