package com.jubasbackend.core.appointment.enums;


import lombok.Getter;

@Getter
public enum AppointmentStatus {
    MARCADO((short) 1), EM_ATENDIMENTO((short) 2), FINALIZADO((short) 3);

    private final Short id;

    AppointmentStatus(short id) {
        this.id = id;
    }

}
