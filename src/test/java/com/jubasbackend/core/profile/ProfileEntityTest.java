package com.jubasbackend.core.profile;

import com.jubasbackend.core.user.UserEntity;

import java.util.UUID;

public class ProfileEntityTest {

    public static ProfileEntity createValidEntity() {
        var user = UserEntity.builder().id(UUID.randomUUID()).build();
        return new ProfileEntity(UUID.randomUUID(), "Juninho Almeida", "12345678910", false, user);
    }
}
