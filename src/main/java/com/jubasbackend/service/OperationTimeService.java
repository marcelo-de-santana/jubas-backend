package com.jubasbackend.service;

import com.jubasbackend.entity.OperationTime;
import com.jubasbackend.repository.OperationTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationTimeService {
    @Autowired
    private OperationTimeRepository operationTimeRepository;

    public OperationTime createNew(OperationTime operationTime) {
        return operationTimeRepository.save(operationTime);
    }
}