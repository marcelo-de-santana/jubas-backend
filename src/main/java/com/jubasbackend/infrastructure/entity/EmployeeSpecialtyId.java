package com.jubasbackend.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class EmployeeSpecialtyId implements Serializable {

    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(name = "specialty_id")
    private UUID specialtyId;
}
