package com.jubasbackend.core.working_hour;

import com.jubasbackend.core.working_hour.dto.WorkingHourResponse;
import com.jubasbackend.core.working_hour.dto.WorkingHourRequest;

import java.util.List;
import java.util.UUID;

public interface WorkingHourService {

    List<WorkingHourResponse> findWorkingHours();

    WorkingHourResponse createWorkingHour(WorkingHourRequest request);

    void updateWorkingHour(UUID workingHourId, WorkingHourRequest request);

    void deleteWorkingHour(UUID workingHourId);

}
