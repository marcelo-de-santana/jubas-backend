package com.jubasbackend.core.employee;

import com.jubasbackend.core.TestEntityFactory;
import com.jubasbackend.core.appointment.AppointmentRepository;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.working_hour.WorkingHourRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class EmployeeServiceBaseTest extends TestEntityFactory {

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> employeeEntityCaptor;

    @Captor
    ArgumentCaptor<LocalDateTime> localDateTimeCaptor;

    @Mock
    protected EmployeeRepository employeeRepository;

    @Mock
    protected ProfileRepository profileRepository;

    @Mock
    protected WorkingHourRepository workingHourRepository;

    @Mock
    protected EmployeeSpecialtyRepository employeeSpecialtyRepository;

    @Mock
    protected AppointmentRepository appointmentRepository;

    @InjectMocks
    protected EmployeeServiceImpl service;

}
