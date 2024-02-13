package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_employee")
public class EmployeeEntity {

    @Id
    @Column(name = "employee_id")
    private UUID id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    private ProfileEntity profile;

    @ManyToOne
    @JoinColumn(name = "working_hour_id")
    private WorkingHourEntity workingHour;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeSpecialtyEntity> specialties;

    public boolean makesSpecialty(UUID specialtyId) {
        var compoundId = new EmployeeSpecialtyId(getId(), specialtyId);
        var compoundEntity = EmployeeSpecialtyEntity.builder().id(compoundId).build();
        return getSpecialties().contains(compoundEntity);
    }

    public boolean isEqual(UUID employeeId) {
        return employeeId.equals(this.getId());
    }

    public SpecialtyEntity getSpecialty(UUID specialtyId) {
        var compoundId = new EmployeeSpecialtyId(getId(), specialtyId);
        var compoundEntity = EmployeeSpecialtyEntity.builder().id(compoundId).build();
        var indexOf = getSpecialties().indexOf(compoundEntity);
        return getSpecialties().get(indexOf).getSpecialty();
    }
}
