package com.jubasbackend.domain.entity;

import com.jubasbackend.dto.ResponseSpecialtyDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity(name = "tb_specialty")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String timeDuration;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    public Specialty() {
    }

    public Specialty(ResponseSpecialtyDTO specialty) {
        this.name = specialty.name();
        this.timeDuration = specialty.timeDuration();
        this.category = new Category(specialty.categoryId());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
