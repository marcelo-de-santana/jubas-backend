package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Payment;
import com.jubasbackend.domain.entity.enums.PaymentMethod;

public record PaymentResponse(AppointmentResponse appointment, PaymentMethod paymentMethod,
                              Long transactionId) {
    public PaymentResponse(Payment payment) {
        this(new AppointmentResponse(payment.getAppointment()),
                payment.getMethod(),
                payment.getTransactionId() != null ? payment.getTransactionId() : null);
    }
}
