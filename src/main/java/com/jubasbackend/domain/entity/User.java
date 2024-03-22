package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.request.AuthRequest;
import com.jubasbackend.controller.request.UserRequest;
import com.jubasbackend.domain.entity.enums.PermissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, length = 50)
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Profile> profiles;

    @NotNull
    private PermissionType permission;

    public void addProfile(String name, String cpf) {
        profiles.add(Profile.builder()
                        .name(name)
                        .cpf(cpf)
                        .statusProfile(true)
                        .build());
    }

    public boolean isCorrectPassword(AuthRequest request, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(request.password(), password);
    }
}
