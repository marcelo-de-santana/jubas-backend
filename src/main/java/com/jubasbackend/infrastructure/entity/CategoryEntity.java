package com.jubasbackend.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jubasbackend.api.dto.request.CategoryRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REFRESH)
    @JsonIgnore
    private List<SpecialtyEntity> specialties = new ArrayList<>();

    public CategoryEntity() {
    }

    public CategoryEntity(Short id) {
        this.id = id;
    }

    public CategoryEntity(CategoryRequest category) {
        this.name = category.name();
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecialtyEntity> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<SpecialtyEntity> specialties) {
        this.specialties = specialties;
    }
}
