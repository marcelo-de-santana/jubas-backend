package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.infrastructure.repository.WorkingHourRepository;
import com.jubasbackend.service.impl.WorkingHourServiceImpl;
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

    protected WorkingHourRequest newWorkingHourRequest(String startTime, String endTime, String startInterval, String endInterval) {
        return new WorkingHourRequest(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
    }

}
