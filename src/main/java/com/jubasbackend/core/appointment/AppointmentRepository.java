package com.jubasbackend.core.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findAllByDate_DateAndEmployeeIdOrClientId(LocalDate date, UUID employeeId, UUID clientId);
}
