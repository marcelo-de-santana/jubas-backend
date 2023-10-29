package com.jubasbackend.domain.entity;

import jakarta.persistence.*;

import java.util.UUID;

import com.jubasbackend.dto.request.RequestProfileDTO;

@Entity(name = "tb_profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true, length = 11)
    private String cpf;

    private boolean statusProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Profile() {
    }

    public Profile(UUID id){
        this.id = id;
    }

    public Profile(RequestProfileDTO requestProfileDTO) {
        this.name = requestProfileDTO.name();
        this.cpf = requestProfileDTO.cpf();
        this.statusProfile = requestProfileDTO.statusProfile();
        this.user = new User(requestProfileDTO.userId());
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isStatusProfile() {
        return statusProfile;
    }

    public void setStatusProfile(boolean statusProfile) {
        this.statusProfile = statusProfile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
