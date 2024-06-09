package com.jubasbackend.domain.entity;

import com.jubasbackend.domain.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_payment")
public class Payment {
    @Id
    @Column(name = "appointment_id")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private PaymentMethod method;

    private Long transactionId;
}
