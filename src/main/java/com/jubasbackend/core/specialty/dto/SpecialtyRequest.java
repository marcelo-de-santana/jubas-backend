package com.jubasbackend.core.specialty.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record SpecialtyRequest(
        String name,
        @JsonFormat(pattern = "HH:mm")
        LocalTime timeDuration,
        Float price,
        Short categoryId) {
    public SpecialtyRequest(String name, String time, float price, short categoryId) {
        this(name, LocalTime.parse(time), price, categoryId);
    }
}
