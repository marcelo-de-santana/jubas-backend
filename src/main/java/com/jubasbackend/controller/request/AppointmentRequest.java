package com.jubasbackend.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentRequest(
        UUID employeeId,

        UUID clientId,

        UUID specialtyId,

        @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm") @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime dateTime,

        AppointmentStatus appointmentStatus,

        PaymentMethod paymentMethod) {

        public LocalTime time() {
                return dateTime.toLocalTime();
        }

        public LocalDate date(){
                return dateTime.toLocalDate();
        }
}
