package com.jubasbackend.dto;

import com.jubasbackend.dto.users.MinimalUserDTO;
import com.jubasbackend.entity.Barber;

import java.util.UUID;

public record BarberDTO(UUID id, String name, boolean statusProfile, MinimalUserDTO user) {

    public BarberDTO(Barber barber){
        this(barber.getId(),barber.getName(),barber.isStatusProfile(), new MinimalUserDTO(barber.getUser()));
    }

}