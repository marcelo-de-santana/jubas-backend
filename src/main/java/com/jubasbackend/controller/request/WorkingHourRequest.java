package com.jubasbackend.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import static java.time.LocalTime.parse;

public record WorkingHourRequest(

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startInterval,

        @Schema(type = "String", pattern = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endInterval
) {

}
