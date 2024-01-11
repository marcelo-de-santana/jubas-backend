package com.jubasbackend.domain.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "tb_employee_specialty")
public class EmployeeSpecialty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private SpecialtyEntity specialty;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public SpecialtyEntity getSpecialty() {
        return specialty;
    }

    public void setSpecialty(SpecialtyEntity specialty) {
        this.specialty = specialty;
    }
}
