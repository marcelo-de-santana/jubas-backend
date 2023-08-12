package com.jubasbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "tb_barber_service")
public class BarberService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}
