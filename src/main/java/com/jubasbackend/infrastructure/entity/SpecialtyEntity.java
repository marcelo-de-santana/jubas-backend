package com.jubasbackend.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.api.dto.request.SpecialtyRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
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

    public SpecialtyEntity(SpecialtyRequest request) {
        this.name = request.name();
        this.price = request.price();
        this.timeDuration = request.timeDuration();
        this.category = CategoryEntity.builder().id(request.categoryId()).build();
    }
}
