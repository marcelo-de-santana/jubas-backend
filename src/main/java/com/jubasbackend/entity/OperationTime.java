package com.jubasbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;
import java.util.UUID;

@Entity(name = "tb_operation_time")
public class OperationTime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Time startTime;
    private Time endTime;
    private Time startInterval;
    private Time endInterval;
}
