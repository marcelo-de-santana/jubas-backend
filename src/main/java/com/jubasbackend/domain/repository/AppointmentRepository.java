package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.jubasbackend.utils.DateTimeUtils.parseEndOfDay;
import static com.jubasbackend.utils.DateTimeUtils.parseStatOfDay;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findAllByDateBetweenAndEmployeeIdOrClientId(LocalDateTime startDate, LocalDateTime endDate, UUID employeeId, UUID clientId);

    List<Appointment> findAllByDateBetweenAndEmployeeId(LocalDateTime startDate, LocalDateTime endDate, UUID employeeId);

}
