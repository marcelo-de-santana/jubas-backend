package com.jubasbackend.entity;

import jakarta.persistence.*;

@Entity(name = "tb_user_permission")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
}
