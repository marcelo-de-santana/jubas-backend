package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.WorkingHourEntity;
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
