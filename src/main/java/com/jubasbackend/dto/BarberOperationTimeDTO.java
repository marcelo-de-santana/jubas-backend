package com.jubasbackend.dto;

import com.jubasbackend.entity.Barber;
import com.jubasbackend.entity.OperationTime;

import java.util.UUID;

public record BarberOperationTimeDTO(UUID id, String name, boolean statusProfile, UserDTO user, OperationTime operationTime) {
    public BarberOperationTimeDTO(Barber barber) {
        this(barber.getId(), barber.getName(), barber.isStatusProfile(), new UserDTO(barber.getUser()), barber.getOperationTime());
    }
}