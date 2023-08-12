package com.jubasbackend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "tb_client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cpf;

    private Boolean statusProfile;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
