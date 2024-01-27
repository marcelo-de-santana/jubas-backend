package com.jubasbackend.core.workingHour.service;

import com.jubasbackend.core.workingHour.WorkingHourRepository;
import com.jubasbackend.core.workingHour.WorkingHourServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.LocalTime.parse;

@ExtendWith(MockitoExtension.class)
public abstract class WorkingHourServiceBaseTest {

    @Mock
    protected WorkingHourRepository repository;

    @InjectMocks
    protected WorkingHourServiceImpl service;

}
