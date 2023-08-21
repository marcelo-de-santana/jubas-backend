package com.jubasbackend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "tb_barber")
public class Barber implements Serializable {

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

    public OperationTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(OperationTime operationTime) {
        this.operationTime = operationTime;
    }
}
