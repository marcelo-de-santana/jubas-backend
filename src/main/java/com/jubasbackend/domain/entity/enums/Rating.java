package com.jubasbackend.domain.entity.enums;

import lombok.Getter;

@Getter
public enum Rating {
    INSATISFEITO((short) 0),
    SATISFEITO((short) 1),
    MUITO_SATISFEITO((short) 2);

    private final Short id;

    Rating(short id) {
        this.id = id;
    }
}
