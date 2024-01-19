package com.jubasbackend.service.impl;

import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import com.jubasbackend.infrastructure.repository.WorkingHoursRepository;
import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.api.dto.response.WorkingHoursResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkingHoursServiceImpl {

    @Autowired
    private WorkingHoursRepository repository;

    protected WorkingHourEntity findWorkingHoursById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    public List<WorkingHoursResponse> findAll() {
        return repository.findAll().stream().map(WorkingHoursResponse::new).toList();
    }

    public WorkingHoursResponse create(WorkingHourRequest workingHoursToCreated) {
        WorkingHourEntity workingHours = new WorkingHourEntity(workingHoursToCreated);
        //Check if working hours are already registered
        if(repository.existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(
                workingHours.getStartTime(), workingHours.getStartInterval(),
                workingHours.getEndInterval(), workingHours.getEndTime())) {
            throw new IllegalArgumentException("Working hours already exists.");
        }
        WorkingHourEntity savedWorkingHours = repository.save(workingHours);
        return new WorkingHoursResponse(savedWorkingHours);
    }

    public void delete(Long id) {
        var WorkingHoursToDelete = findWorkingHoursById(id);
        repository.delete(WorkingHoursToDelete);
    }


}
