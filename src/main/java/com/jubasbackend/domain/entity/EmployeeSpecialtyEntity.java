package com.jubasbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity(name = "tb_employee_associated_specialty")
public class EmployeeSpecialtyEntity {

    @EmbeddedId
    private EmployeeSpecialtyId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ManyToOne
    @MapsId("specialtyId")
    @JoinColumn(name = "specialty_id")
    private SpecialtyEntity specialty;

    public static EmployeeSpecialtyEntity create(UUID employeeId, UUID specialtyId) {
        var compoundId = new EmployeeSpecialtyId(employeeId, specialtyId);
        return EmployeeSpecialtyEntity.builder().id(compoundId).build();
    }

}
