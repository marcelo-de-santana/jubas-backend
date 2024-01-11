package com.jubasbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.api.dto.request.WorkingHoursRequest;
import com.jubasbackend.utils.TimeUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity(name = "tb_working_hours")
public class WorkingHoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startInterval;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endInterval;

    public WorkingHoursEntity() {
    }

    public WorkingHoursEntity(Long id) {
        this.id = id;
    }

    public WorkingHoursEntity(WorkingHoursRequest workingHours) {
        this.startTime = TimeUtils.parseToLocalTime(workingHours.startTime());
        this.startInterval = TimeUtils.parseToLocalTime(workingHours.startInterval());
        this.endInterval = TimeUtils.parseToLocalTime(workingHours.endInterval());
        this.endTime = TimeUtils.parseToLocalTime(workingHours.endTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartInterval() {
        return startInterval;
    }

    public void setStartInterval(LocalTime startInterval) {
        this.startInterval = startInterval;
    }

    public LocalTime getEndInterval() {
        return endInterval;
    }

    public void setEndInterval(LocalTime endInterval) {
        this.endInterval = endInterval;
    }
}
