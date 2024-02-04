package com.jubasbackend.core.appointment;

import com.jubasbackend.core.TestEntityFactory;
import com.jubasbackend.core.employee.EmployeeRepository;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public abstract class AppointmentServiceBaseTest extends TestEntityFactory {

    @Mock
    protected AppointmentRepository appointmentRepository;

    @Mock
    protected EmployeeSpecialtyRepository employeeSpecialtyRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    protected AppointmentServiceImpl service;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<LocalDateTime> localDateTimeCaptor;

    @Captor
    ArgumentCaptor<AppointmentEntity> appointmentEntityCaptor;



}
