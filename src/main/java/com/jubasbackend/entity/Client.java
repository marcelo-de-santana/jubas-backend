package com.jubasbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity(name = "tb_client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true, length = 11)
    @NotNull
    private Long cpf;

    private Boolean statusProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Boolean getStatusProfile() {
        return statusProfile;
    }

    public void setStatusProfile(Boolean statusProfile) {
        this.statusProfile = statusProfile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}