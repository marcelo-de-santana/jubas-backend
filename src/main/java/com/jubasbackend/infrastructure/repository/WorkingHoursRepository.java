package com.jubasbackend.infrastructure.repository;

import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHourEntity, Long> {
    boolean existsByStartTimeAndStartIntervalAndEndIntervalAndEndTime(LocalTime startTime, LocalTime startInterval, LocalTime endInterval, LocalTime endTime);
}
