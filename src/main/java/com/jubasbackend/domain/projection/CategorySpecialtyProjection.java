package com.jubasbackend.domain.projection;

import java.util.UUID;

public interface CategorySpecialtyProjection {

    Short getCategoryId();

    String getCategoryName();

    UUID getSpecialtyId();

    String getSpecialtyName();

    Float getSpecialtyPrice();
    String getSpecialtyTimeDuration();
}
