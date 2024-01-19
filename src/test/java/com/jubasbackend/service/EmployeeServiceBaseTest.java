package com.jubasbackend.service;

import com.jubasbackend.infrastructure.repository.EmployeeRepository;
import com.jubasbackend.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class EmployeeServiceBaseTest {

    @Mock
    protected EmployeeRepository repository;

    @InjectMocks
    protected EmployeeServiceImpl service;

}
