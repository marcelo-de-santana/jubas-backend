package com.jubasbackend.service.appointment;

import com.jubasbackend.service.TestEntityFactory;
import com.jubasbackend.domain.repository.DayAvailabilityRepository;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.service.AppointmentService;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.EmployeeSpecialtyRepository;
import com.jubasbackend.domain.repository.NonServiceDayRepository;
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
public abstract class AbstractAppointmentServiceTest extends TestEntityFactory {

    @Mock
    protected AppointmentRepository appointmentRepository;

    @Mock
    protected EmployeeSpecialtyRepository employeeSpecialtyRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    NonServiceDayRepository nonServiceDayRepository;

    @Mock
    DayAvailabilityRepository dayAvailabilityRepository;

    @InjectMocks
    protected AppointmentService service;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<LocalDateTime> dateTimeCaptor;

    @Captor
    ArgumentCaptor<Appointment> appointmentEntityCaptor;

}
