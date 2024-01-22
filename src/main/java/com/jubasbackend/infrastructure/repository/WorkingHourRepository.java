package com.jubasbackend.infrastructure.repository;

import com.jubasbackend.infrastructure.entity.WorkingHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHourEntity, UUID> {
    boolean existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
            LocalTime startTime,
            LocalTime endTime,
            LocalTime startInterval,
            LocalTime endInterval);
}
