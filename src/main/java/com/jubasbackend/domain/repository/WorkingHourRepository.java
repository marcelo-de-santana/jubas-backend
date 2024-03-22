package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, UUID> {
    default boolean areTimesExists(LocalTime startTime, LocalTime endTime, LocalTime startInterval, LocalTime endInterval) {
        return existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(startTime, endTime, startInterval, endInterval);
    }

    boolean existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
            LocalTime startTime,
            LocalTime endTime,
            LocalTime startInterval,
            LocalTime endInterval);
}
