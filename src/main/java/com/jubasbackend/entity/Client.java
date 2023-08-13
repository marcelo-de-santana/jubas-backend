package com.jubasbackend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "tb_client")
public class Client implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true, length = 11)
    private Long cpf;

    private Boolean statusProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
