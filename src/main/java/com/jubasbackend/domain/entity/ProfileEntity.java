package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.request.ProfileUserRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @NotBlank
    private String name;

    @Column(unique = true)
    @Size(min = 11, max = 11)
    private String cpf;

    private boolean statusProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "profile")
    private EmployeeEntity employee;

    public ProfileEntity(ProfileUserRequest request) {
        this.name = request.name();
        this.cpf = request.cpf();
        this.statusProfile = request.statusProfile();
        this.user = UserEntity.builder().id(request.userId()).build();
    }

}
