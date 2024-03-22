package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.response.ScheduleTimeResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_employee")
public class Employee {

    @Id
    @Column(name = "employee_id")
    private UUID id;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "working_hour_id")
    private WorkingHour workingHour;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeSpecialty> specialties;

    public boolean makesSpecialty(UUID specialtyId) {
        return getSpecialties().contains(getCompoundEntity(specialtyId));
    }

    public boolean hasId(UUID employeeId) {
        return employeeId.equals(this.getId());
    }

    public Specialty getSpecialty(UUID specialtyId) {
        var indexOf = getSpecialties().indexOf(getCompoundEntity(specialtyId));
        return getSpecialties().get(indexOf).getSpecialty();
    }

    public List<ScheduleTimeResponse> getPossibleTimes(UUID specialtyId, List<Appointment> appointments) {
        return workingHour.getPossibleTimes(getSpecialty(specialtyId), appointments);
    }

    public void addSpecialty(UUID specialtyId) {
        getSpecialties().add(getCompoundEntity(specialtyId));
    }

    public void removeSpecialty(UUID specialtyId) {
        getSpecialties().remove(getCompoundEntity(specialtyId));
    }

    private EmployeeSpecialty getCompoundEntity(UUID specialtyId) {
        return EmployeeSpecialty.create(getId(), specialtyId);
    }
}
