package com.jubasbackend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true,length = 50)
    private String email;

    private String password;

    @OneToOne
    private UserPermission userPermission;
}
