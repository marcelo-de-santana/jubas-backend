package com.jubasbackend.service.user;

import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractUserServiceTest {

    @Mock
    protected UserRepository repository;

    @Mock
    protected PasswordEncoder passwordEncoder;

    @Mock
    JwtAuthenticationToken token;

    @InjectMocks
    protected UserService service;

}
