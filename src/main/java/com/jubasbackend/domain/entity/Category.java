package com.jubasbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jubasbackend.dto.request.CategoryRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REFRESH)
    @JsonIgnore
    private List<Specialty> specialties = new ArrayList<>();

    public Category() {
    }

    public Category(Short id) {
        this.id = id;
    }

    public Category(CategoryRequest category) {
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

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }
}
