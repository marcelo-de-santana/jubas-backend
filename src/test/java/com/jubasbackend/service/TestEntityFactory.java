package com.jubasbackend.service;

import com.jubasbackend.domain.entity.WorkingHour;
import com.jubasbackend.controller.request.WorkingHourRequest;

import static java.time.LocalTime.parse;

public abstract class TestEntityFactory {

    protected WorkingHour createWorkingHour(String startTime, String endTime, String startInterval, String endInterval) {
        return new WorkingHour(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
    }

    protected WorkingHourRequest createWorkingHourRequest(String startTime, String endTime, String startInterval, String endInterval) {
        return new WorkingHourRequest(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
    }
}
