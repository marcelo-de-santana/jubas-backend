package com.jubasbackend.service;

import com.jubasbackend.infrastructure.repository.EmployeeRepository;
import com.jubasbackend.infrastructure.repository.EmployeeSpecialtyRepository;
import com.jubasbackend.infrastructure.repository.ProfileRepository;
import com.jubasbackend.infrastructure.repository.WorkingHourRepository;
import com.jubasbackend.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class EmployeeServiceBaseTest {

    @Mock
    protected EmployeeRepository employeeRepository;

    @Mock
    protected ProfileRepository profileRepository;

    @Mock
    protected WorkingHourRepository workingHourRepository;

    @Mock
    protected EmployeeSpecialtyRepository employeeSpecialtyRepository;

    @InjectMocks
    protected EmployeeServiceImpl service;

}
