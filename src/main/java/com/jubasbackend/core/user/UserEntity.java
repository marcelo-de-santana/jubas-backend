package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserPermissionRequest;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.user.enums.PermissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, length = 50)
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ProfileEntity> profile;

    @NotNull
    private PermissionType permission;

    public UserEntity(UserPermissionRequest request) {
        this.email = request.email();
        this.password = request.password();
        this.permission = request.permission();
    }
}
