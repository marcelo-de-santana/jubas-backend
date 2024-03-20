package com.jubasbackend.service.working_hour;

import com.jubasbackend.service.TestEntityFactory;
import com.jubasbackend.domain.repository.WorkingHourRepository;
import com.jubasbackend.service.WorkingHourService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractWorkingHourServiceTest extends TestEntityFactory {

    @Mock
    protected WorkingHourRepository repository;

    @InjectMocks
    protected WorkingHourService service;

}
