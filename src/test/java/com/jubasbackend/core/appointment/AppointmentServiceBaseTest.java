package com.jubasbackend.core.appointment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public abstract class AppointmentServiceBaseTest {

    @Mock
    protected AppointmentRepository appointmentRepository;

    @InjectMocks
    protected AppointmentServiceImpl service;
}
