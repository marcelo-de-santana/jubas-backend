package com.jubasbackend.domain.entity;

import com.jubasbackend.dto.request.UserRequest;
import jakarta.persistence.*;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, length = 50)
    @NotNull
    private String email;

    @NotNull
    @Size(min = 8, max = 20)
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
}
