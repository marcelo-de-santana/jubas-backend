package com.jubasbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "tb_barber_services")
public class BarberServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

}