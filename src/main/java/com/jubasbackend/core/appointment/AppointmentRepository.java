package com.jubasbackend.core.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findAllByDateAndEmployeeId(LocalDateTime dateTime, UUID employeeId);


    List<AppointmentEntity> findAllByDateAndEmployeeIdOrClientId(LocalDateTime date, UUID employeeId, UUID clientId);
}
