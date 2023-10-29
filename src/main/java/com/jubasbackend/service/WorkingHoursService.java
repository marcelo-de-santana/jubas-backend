package com.jubasbackend.service;

import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.domain.repository.WorkingHoursRepository;
import com.jubasbackend.dto.request.RequestWorkingHoursDTO;
import com.jubasbackend.dto.response.ResponseWorkingHoursDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkingHoursService {

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

    protected WorkingHours findWorkingHoursById(Long id) {
        return workingHoursRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    public List<ResponseWorkingHoursDTO> findAll() {
        return workingHoursRepository.findAll().stream().map(ResponseWorkingHoursDTO::new).toList();
    }

    public ResponseWorkingHoursDTO create(RequestWorkingHoursDTO workingHoursToCreated) {
        WorkingHours workingHours = new WorkingHours(workingHoursToCreated);
        //Check if working hours are already registered
        if(workingHoursRepository.existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(
                workingHours.getStartTime(), workingHours.getStartInterval(),
                workingHours.getEndInterval(), workingHours.getEndTime())) {
            throw new IllegalArgumentException("Working hours already exists.");
        }
        WorkingHours savedWorkingHours = workingHoursRepository.save(workingHours);
        return new ResponseWorkingHoursDTO(savedWorkingHours);
    }

    public ResponseWorkingHoursDTO delete(Long id) {
        var WorkingHoursToDelete = findWorkingHoursById(id);
        workingHoursRepository.delete(WorkingHoursToDelete);
        return new ResponseWorkingHoursDTO(WorkingHoursToDelete);
    }


}
