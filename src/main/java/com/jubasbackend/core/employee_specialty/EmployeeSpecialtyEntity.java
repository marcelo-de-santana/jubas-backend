package com.jubasbackend.core.employee_specialty;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

}
