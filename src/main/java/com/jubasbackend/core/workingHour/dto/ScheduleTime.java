package com.jubasbackend.core.workingHour.dto;

import java.time.LocalTime;

public interface ScheduleTime {
    LocalTime time();

    boolean isAvailable();


}
