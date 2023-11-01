package com.jubasbackend.service;

import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.domain.repository.WorkingHoursRepository;
import com.jubasbackend.dto.request.WorkingHoursRequest;
import com.jubasbackend.dto.response.WorkingHoursResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkingHoursService {

    @Autowired
    private WorkingHoursRepository repository;

    protected WorkingHours findWorkingHoursById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    public List<WorkingHoursResponse> findAll() {
        return repository.findAll().stream().map(WorkingHoursResponse::new).toList();
    }

    public WorkingHoursResponse create(WorkingHoursRequest workingHoursToCreated) {
        WorkingHours workingHours = new WorkingHours(workingHoursToCreated);
        //Check if working hours are already registered
        if(repository.existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(
                workingHours.getStartTime(), workingHours.getStartInterval(),
                workingHours.getEndInterval(), workingHours.getEndTime())) {
            throw new IllegalArgumentException("Working hours already exists.");
        }
        WorkingHours savedWorkingHours = repository.save(workingHours);
        return new WorkingHoursResponse(savedWorkingHours);
    }

    public void delete(Long id) {
        var WorkingHoursToDelete = findWorkingHoursById(id);
        repository.delete(WorkingHoursToDelete);
    }


}
