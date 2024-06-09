package com.jubasbackend.domain.entity.enums;


import lombok.Getter;

@Getter
public enum AppointmentStatus {
    CANCELADO((short) 0), MARCADO((short) 1), EM_ATENDIMENTO((short) 2), FINALIZADO((short) 3), PAGO((short) 4), AVALIADO((short) 5);

    private final Short id;

    AppointmentStatus(short id) {
        this.id = id;
    }

}
