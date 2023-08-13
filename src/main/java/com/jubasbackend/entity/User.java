package com.jubasbackend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "tb_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, length = 50)
    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "user_permission_id")
    private UserPermission userPermission;
}
