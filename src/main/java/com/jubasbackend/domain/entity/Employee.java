package com.jubasbackend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Entity(name = "tb_employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "profile_id")
    @NotNull
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "operation_time_id")
    private OperationTime operationTime;
}
