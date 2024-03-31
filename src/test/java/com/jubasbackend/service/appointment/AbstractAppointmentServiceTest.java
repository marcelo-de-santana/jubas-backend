package com.jubasbackend.service.appointment;

import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.repository.*;
import com.jubasbackend.service.AppointmentService;
import com.jubasbackend.service.TestEntityFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<LocalDateTime> dateTimeCaptor;

    @Captor
    ArgumentCaptor<Appointment> appointmentEntityCaptor;

    @InjectMocks
    protected AppointmentService service;

}
