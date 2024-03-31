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
public class EmployeeSpecialty {

    @EmbeddedId
    private EmployeeSpecialtyId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("specialtyId")
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    public static EmployeeSpecialty create(UUID employeeId, UUID specialtyId) {
        var compoundId = new EmployeeSpecialtyId(employeeId, specialtyId);
        return EmployeeSpecialty.builder()
                .id(compoundId)
                .employee(Employee.builder()
                        .id(employeeId)
                        .build())
                .specialty(Specialty.builder()
                        .id(specialtyId)
                        .build())
                .build();
    }

}
