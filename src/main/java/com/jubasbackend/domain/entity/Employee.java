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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private List<EmployeeSpecialty> specialTies;

    @ManyToOne
    @JoinColumn(name = "working_hours_id")
    private WorkingHours workingHours;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<EmployeeSpecialty> getServices() {
        return specialTies;
    }

    public void setServices(List<EmployeeSpecialty> services) {
        this.specialTies = services;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }
}
