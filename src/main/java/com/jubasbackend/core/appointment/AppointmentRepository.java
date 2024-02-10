package com.jubasbackend.core.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<AppointmentEntity> findAllByDateBetweenAndEmployeeIdOrClientId(LocalDateTime startDate, LocalDateTime endDate, UUID employeeId, UUID clientId);

    List<AppointmentEntity> findAllByDateBetweenAndEmployeeId(LocalDateTime startDate, LocalDateTime endDate, UUID employeeId);

}
