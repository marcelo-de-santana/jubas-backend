package com.jubasbackend.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record SpecialtyRequest(
        String name,
        @Schema(type = "string", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime timeDuration,
        Float price,
        Short categoryId) {
}
