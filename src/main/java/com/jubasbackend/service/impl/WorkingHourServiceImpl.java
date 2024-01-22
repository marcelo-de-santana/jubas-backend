package com.jubasbackend.service.impl;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.api.dto.response.WorkingHourResponse;
import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.infrastructure.repository.WorkingHourRepository;
import com.jubasbackend.service.WorkingHourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingHourServiceImpl implements WorkingHourService {

    private final WorkingHourRepository repository;

    public boolean areTimesRegisteredOnRepository(WorkingHourEntity entity) {
        return repository.existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                entity.getStartTime(), entity.getEndTime(), entity.getStartInterval(), entity.getEndInterval());
    }

    public WorkingHourEntity findWorkingHourOnRepository(UUID workingHourId) {
        return repository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    @Override
    public List<WorkingHourResponse> findWorkingHours() {
        return repository.findAll().stream().map(WorkingHourResponse::new).toList();
    }

    @Override
    public WorkingHourResponse createWorkingHour(WorkingHourRequest request) {
        var newWorkingHour = new WorkingHourEntity(request);
        newWorkingHour.validate();

        if (areTimesRegisteredOnRepository(newWorkingHour))
            throw new IllegalArgumentException("Working hours already exists.");

        return new WorkingHourResponse(repository.save(newWorkingHour));
    }

    @Override
    public void updateWorkingHour(UUID workingHourId, WorkingHourRequest request) {
        var workingHour = findWorkingHourOnRepository(workingHourId);
        workingHour.update(request);
        workingHour.validate();
        repository.save(workingHour);
    }

    @Override
    public void deleteWorkingHour(UUID workingHourId) {
        repository.deleteById(workingHourId);
    }


}
