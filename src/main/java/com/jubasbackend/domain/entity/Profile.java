package com.jubasbackend.domain.entity;

import com.jubasbackend.dto.request.ProfileRequest;
import jakarta.persistence.*;
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

    private String name;

    @Column(unique = true)
    @Size(min = 11, max = 11)
    private String cpf;

    private boolean statusProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Profile(UUID id){
        this.id = id;
    }

    public Profile(ProfileRequest requestProfileDTO) {
        this.name = requestProfileDTO.name();
        this.cpf = requestProfileDTO.cpf();
        this.statusProfile = requestProfileDTO.statusProfile();
        this.user = new User(requestProfileDTO.userId());
    }

}
