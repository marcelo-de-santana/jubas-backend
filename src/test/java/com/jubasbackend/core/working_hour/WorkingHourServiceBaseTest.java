package com.jubasbackend.core.working_hour;

import com.jubasbackend.core.TestEntityFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class WorkingHourServiceBaseTest extends TestEntityFactory {

    @Mock
    protected WorkingHourRepository repository;

    @InjectMocks
    protected WorkingHourServiceImpl service;

}
