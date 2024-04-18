package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.request.ProfileRequest;
import jakarta.persistence.*;
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
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String name;

    @Column(unique = true)
    @Size(min = 11, max = 11)
    private String cpf;

    private boolean statusProfile;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "profile")
    private Employee employee;

    public Profile(ProfileRequest request) {
        this.name = request.name();
        this.cpf = request.cpf();
        this.statusProfile = request.statusProfile();
        this.user = User.builder()
                .id(request.userId())
                .build();
    }

}
