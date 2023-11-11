package com.jubasbackend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import com.jubasbackend.dto.request.UserRequest;

@Entity(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, length = 50)
    @NotNull
    private String email;

    @NotNull
    private String password;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "user")
    private List<Profile> profile;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_permission_id")
    private UserPermission userPermission;

    public User(UUID id) {
        this.id = id;
    }

    public User(UserRequest requestUserDTO) {
        this.email = requestUserDTO.email();
        this.password = requestUserDTO.password();
        this.userPermission = new UserPermission(requestUserDTO.userPermissionId());
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Profile> getProfile() {
        return profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public UserPermission getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermission userPermission) {
        this.userPermission = userPermission;
    }

}
