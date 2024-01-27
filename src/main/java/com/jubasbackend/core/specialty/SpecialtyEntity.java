package com.jubasbackend.core.specialty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.category.CategoryEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.specialty.dto.SpecialtyRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_specialty")
public class SpecialtyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private Float price;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeDuration;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "specialty", cascade = CascadeType.REMOVE)
    private List<EmployeeSpecialtyEntity> employees;

    public SpecialtyEntity(SpecialtyRequest request) {
        this.name = request.name();
        this.price = request.price();
        this.timeDuration = request.timeDuration();
        this.category = CategoryEntity.builder().id(request.categoryId()).build();
    }
}
