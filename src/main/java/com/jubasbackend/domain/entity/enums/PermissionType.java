package com.jubasbackend.domain.entity.enums;

import lombok.Getter;

@Getter
public enum PermissionType {
    ADMIN((short)0), BARBEIRO((short)1), CLIENTE((short)2);

    private final Short id;

    PermissionType(Short id){
        this.id = id;
    }
}
