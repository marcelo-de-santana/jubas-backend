package com.jubasbackend.infrastructure.entity;

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

}
