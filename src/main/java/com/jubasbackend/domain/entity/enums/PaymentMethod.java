package com.jubasbackend.domain.entity.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    DINHEIRO((short) 0), CREDITO((short) 1), DEBITO((short) 2);

    private final short id;

    PaymentMethod(short id) {
        this.id = id;
    }
}
