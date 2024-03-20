package com.jubasbackend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tb_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @NotNull
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SpecialtyEntity> specialties;

}
