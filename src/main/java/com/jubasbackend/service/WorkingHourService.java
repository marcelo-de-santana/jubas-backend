package com.jubasbackend.service;

import com.jubasbackend.controller.request.WorkingHourRequest;
import com.jubasbackend.controller.response.WorkingHourResponse;
import com.jubasbackend.domain.entity.WorkingHourEntity;
import com.jubasbackend.domain.repository.WorkingHourRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingHourService {

    private final WorkingHourRepository repository;

    public List<WorkingHourResponse> findWorkingHours() {
        return repository.findAll().stream().map(WorkingHourResponse::new).toList();
    }

    public WorkingHourResponse createWorkingHour(WorkingHourRequest request) {
        var newWorkingHour = new WorkingHourEntity(request);

        if (areTimesRegisteredOnRepository(newWorkingHour))
            throw new APIException(HttpStatus.CONFLICT, "Working hours already exists.");

        return new WorkingHourResponse(repository.save(newWorkingHour));
    }

    public void updateWorkingHour(UUID workingHourId, WorkingHourRequest request) {
        var workingHour = findWorkingHourOnRepository(workingHourId);
        workingHour.update(request);
        repository.save(workingHour);
    }

    public void deleteWorkingHour(UUID workingHourId) {
        repository.deleteById(workingHourId);
    }

    private WorkingHourEntity findWorkingHourOnRepository(UUID workingHourId) {
        return repository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    private boolean areTimesRegisteredOnRepository(WorkingHourEntity entity) {
        return repository.existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                entity.getStartTime(), entity.getEndTime(), entity.getStartInterval(), entity.getEndInterval());
    }
}
