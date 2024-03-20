package com.jubasbackend.service;

import com.jubasbackend.domain.entity.WorkingHourEntity;
import com.jubasbackend.controller.request.WorkingHourRequest;

import static java.time.LocalTime.parse;

public abstract class TestEntityFactory {

    protected WorkingHourEntity createWorkingHour(String startTime, String endTime, String startInterval, String endInterval) {
        return new WorkingHourEntity(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
    }

    protected WorkingHourRequest createWorkingHourRequest(String startTime, String endTime, String startInterval, String endInterval) {
        return new WorkingHourRequest(parse(startTime), parse(endTime), parse(startInterval), parse(endInterval));
    }
}
