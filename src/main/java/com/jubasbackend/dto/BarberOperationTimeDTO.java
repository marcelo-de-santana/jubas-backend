package com.jubasbackend.dto;

import com.jubasbackend.dto.users.MinimalUserDTO;
import com.jubasbackend.entity.Barber;
import com.jubasbackend.entity.OperationTime;

import java.util.UUID;

public record BarberOperationTimeDTO(UUID id, String name, boolean statusProfile, MinimalUserDTO user, OperationTime operationTime) {
    public BarberOperationTimeDTO(Barber barber) {
        this(barber.getId(), barber.getName(), barber.isStatusProfile(), new MinimalUserDTO(barber.getUser()), barber.getOperationTime());
    }
}