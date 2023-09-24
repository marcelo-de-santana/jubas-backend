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
}
