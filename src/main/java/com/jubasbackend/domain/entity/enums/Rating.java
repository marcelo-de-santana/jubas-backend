package com.jubasbackend.domain.entity.enums;

import lombok.Getter;

@Getter
public enum Rating {
    MUITO_RUIM((short) 0),
    RUIM((short) 1),
    REGULAR((short) 2),
    BOM((short) 3),
    EXCELENTE((short) 4);

    private final Short id;

    Rating(short id) {
        this.id = id;
    }
}
