package com.jubasbackend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "tb_barber")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private boolean statusProfile;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToOne
    @JoinColumn(name = "id_operation_time")
    private OperationTime operationTime;
}
