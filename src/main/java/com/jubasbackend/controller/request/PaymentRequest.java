package com.jubasbackend.controller.request;

import com.jubasbackend.domain.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentRequest(@NotNull
                             UUID appointmentId,
                             @NotNull
                             PaymentMethod paymentMethod,
                             String cardToken,
                             String paymentMethodId,
                             String identificationType,
                             String identificationNumber
) {
}
