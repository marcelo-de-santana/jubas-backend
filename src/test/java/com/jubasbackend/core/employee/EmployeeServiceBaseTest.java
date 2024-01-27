package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.EmployeeRepository;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.workingHour.WorkingHourRepository;
import com.jubasbackend.core.employee.EmployeeServiceImpl;
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
