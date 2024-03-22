package com.jubasbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.controller.request.SpecialtyRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_specialty")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private BigDecimal price;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeDuration;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "specialty", cascade = CascadeType.REMOVE)
    private List<EmployeeSpecialty> employees;

    public Specialty(SpecialtyRequest request) {
        this.name = request.name();
        this.price = request.price();
        this.timeDuration = request.timeDuration();
        this.category = Category.builder()
                .id(request.categoryId())
                .build();
    }
}
