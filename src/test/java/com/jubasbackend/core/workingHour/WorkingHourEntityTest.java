package com.jubasbackend.core.workingHour;

import static java.time.LocalTime.parse;

public class WorkingHourEntityTest {

    public static WorkingHourEntity createValidEntity() {
        return createNew("07:00", "16:00", "11:00", "12:00");
    }

    public static WorkingHourEntity createNew(String startTime, String endTime, String startInterval, String endInterval) {
        return new WorkingHourEntity(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
    }

}
