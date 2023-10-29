package com.jubasbackend.domain.entity;

import com.jubasbackend.dto.request.RequestCategoryDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Short id;

    @NotNull
    private String name;

    public Category() {
    }

    public Category(Short id) {
        this.id = id;
    }

    public Category(RequestCategoryDTO category) {
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

}
