package com.jubasbackend.infrastructure.entity;

import com.jubasbackend.api.dto.request.EmployeeCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    public EmployeeEntity(EmployeeCreateRequest request) {
        this.id = request.profileId();
        this.profile = ProfileEntity.builder().id(request.profileId()).build();
        this.workingHour = WorkingHourEntity.builder().id(request.workingHourId()).build();
        this.specialties = new ArrayList<>();
    }
}
