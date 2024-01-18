package com.jubasbackend.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Entity(name = "tb_employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "profile_id")
    @NotNull
    private ProfileEntity profile;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private List<EmployeeSpecialty> specialTies;

    @ManyToOne
    @JoinColumn(name = "working_hours_id")
    private WorkingHoursEntity workingHours;

    public EmployeeEntity(){
    }
    public EmployeeEntity(ProfileEntity profile){
        this.profile = profile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(ProfileEntity profile) {
        this.profile = profile;
    }

    public List<EmployeeSpecialty> getServices() {
        return specialTies;
    }

    public void setServices(List<EmployeeSpecialty> services) {
        this.specialTies = services;
    }

    public WorkingHoursEntity getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHoursEntity workingHours) {
        this.workingHours = workingHours;
    }
}
