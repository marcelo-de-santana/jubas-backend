package com.jubasbackend.service.impl;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.api.dto.response.WorkingHourResponse;
import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.infrastructure.repository.WorkingHourRepository;
import com.jubasbackend.service.WorkingHourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingHourServiceImpl implements WorkingHourService {

    private final WorkingHourRepository repository;

    public WorkingHourEntity findWorkingHourOnRepository(UUID workingHourId) {
        return repository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    public boolean areTimesRegisteredOnRepository(LocalTime startTime, LocalTime startInterval, LocalTime endInterval, LocalTime endTime) {
        return repository.existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(startTime, startInterval, endInterval, endTime);
    }

    @Override
    public List<WorkingHourResponse> findWorkingHours() {
        return repository.findAll().stream().map(WorkingHourResponse::new).toList();
    }

    @Override
    public WorkingHourResponse createWorkingHour(WorkingHourRequest request) {
        if (areTimesRegisteredOnRepository(request.startTime(), request.startInterval(), request.endInterval(), request.endTime()))
            throw new IllegalArgumentException("Working hours already exists.");

        var newWorkingHour = new WorkingHourEntity(request);
        newWorkingHour.validate();

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
