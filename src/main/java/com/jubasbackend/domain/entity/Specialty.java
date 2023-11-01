package com.jubasbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jubasbackend.dto.request.SpecialtyRequest;
import com.jubasbackend.utils.TimeUtils;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity(name = "tb_specialty")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private Float price;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeDuration;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @JsonIgnore
    private Category category;

    public Specialty() {
    }

    public Specialty(SpecialtyRequest specialty) {
        this.name = specialty.name();
        this.timeDuration = TimeUtils.parseToLocalTime(specialty.timeDuration());
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalTime getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(LocalTime timeDuration) {
        this.timeDuration = timeDuration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
