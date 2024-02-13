package com.jubasbackend.core.appointment.enums;


import lombok.Getter;

@Getter
public enum AppointmentStatus {
    MARCADO((short) 0), EM_ATENDIMENTO((short) 1), FINALIZADO((short) 2), CANCELADO((short) 3);

    private final Short id;

    AppointmentStatus(short id) {
        this.id = id;
    }

}
