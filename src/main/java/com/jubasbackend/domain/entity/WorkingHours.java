package com.jubasbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.dto.request.RequestWorkingHoursDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "tb_working_hours")
public class WorkingHours {
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

    public WorkingHours() {
    }

    public WorkingHours(Long id) {
        this.id = id;
    }

    public WorkingHours(RequestWorkingHoursDTO workingHours) {
        this.startTime = timeParser(workingHours.startTime());
        this.startInterval = timeParser(workingHours.startInterval());
        this.endInterval = timeParser(workingHours.endInterval());
        this.endTime = timeParser(workingHours.endTime());
    }

    public LocalTime timeParser(String time){
        return LocalTime.parse(time);
    }

    public String timeFormatter(LocalTime time) {
        return DateTimeFormatter.ofPattern("HH:mm").format(time);
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