package com.jubasbackend.controller;

import com.jubasbackend.entity.OperationTime;
import com.jubasbackend.service.OperationTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/operation-time")
public class OperationTimeController {
    @Autowired
    private OperationTimeService operationTimeService;

    @PostMapping
    public ResponseEntity<OperationTime> create(@RequestBody OperationTime operationTime) {
        return ResponseEntity.status(HttpStatus.OK).body(operationTimeService.createNew(operationTime));
    }

    @GetMapping
    public ResponseEntity<List<OperationTime>> findAll(){
        return ResponseEntity.ok(operationTimeService.findAll());
    }
}