package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.api.dto.response.WorkingHourResponse;

import java.util.List;
import java.util.UUID;

public interface WorkingHourService {

    List<WorkingHourResponse> findWorkingHours();

    WorkingHourResponse createWorkingHour(WorkingHourRequest request);

    void updateWorkingHour(UUID workingHourId, WorkingHourRequest request);

    void deleteWorkingHour(UUID workingHourId);

}
