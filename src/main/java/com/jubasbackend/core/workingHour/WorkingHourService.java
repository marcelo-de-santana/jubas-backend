package com.jubasbackend.core.workingHour;

import com.jubasbackend.core.workingHour.dto.WorkingHourResponse;
import com.jubasbackend.core.workingHour.dto.WorkingHourRequest;

import java.util.List;
import java.util.UUID;

public interface WorkingHourService {

    List<WorkingHourResponse> findWorkingHours();

    WorkingHourResponse createWorkingHour(WorkingHourRequest request);

    void updateWorkingHour(UUID workingHourId, WorkingHourRequest request);

    void deleteWorkingHour(UUID workingHourId);

}
