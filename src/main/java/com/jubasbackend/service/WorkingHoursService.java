package com.jubasbackend.service;

import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.domain.repository.WorkingHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkingHoursService {

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

    public List<WorkingHours> findAll(){
        return workingHoursRepository.findAll();
    }

    public WorkingHours create(WorkingHours workingHours) {
        boolean workingHourExists = workingHoursRepository.existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(workingHours.getStartTime(), workingHours.getStartInterval(), workingHours.getEndInterval(), workingHours.getEndTime());
        if (!workingHourExists) {
            return workingHoursRepository.save(workingHours);
        }
        throw new IllegalArgumentException("Working hour already exists.");
    }
}
