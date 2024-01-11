package com.jubasbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_permission")

public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private String type;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<UserEntity> users;
}
