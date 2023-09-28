package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    boolean existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(LocalTime startTime, LocalTime startInterval, LocalTime endInterval, LocalTime endTime);
}
