package com.jubasbackend.core.employee;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;
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
        return getSpecialties().contains(getCompoundEntity(specialtyId));
    }

    public boolean hasId(UUID employeeId) {
        return employeeId.equals(this.getId());
    }

    public SpecialtyEntity retrieveSpecialty(UUID specialtyId) {
        var indexOf = getSpecialties().indexOf(getCompoundEntity(specialtyId));
        return getSpecialties().get(indexOf).getSpecialty();
    }

    public List<ScheduleTimeResponse> getPossibleTimes(UUID specialtyId, List<AppointmentEntity> appointments) {
        return workingHour.getPossibleTimes(retrieveSpecialty(specialtyId), appointments);
    }

    public void addSpecialty(UUID specialtyId) {
        getSpecialties().add(getCompoundEntity(specialtyId));
    }

    public void removeSpecialty(UUID specialtyId) {
        getSpecialties().remove(getCompoundEntity(specialtyId));
    }

    private EmployeeSpecialtyEntity getCompoundEntity(UUID specialtyId) {
        return EmployeeSpecialtyEntity.create(getId(), specialtyId);
    }
}
