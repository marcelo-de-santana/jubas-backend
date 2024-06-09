package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findAllByDateBetweenAndEmployeeIdOrClientId(LocalDateTime startDate, LocalDateTime endDate, UUID employeeId, UUID clientId);

    List<Appointment> findAllByDateBetweenAndEmployeeId(LocalDateTime startDate, LocalDateTime endDate, UUID employeeId);

    List<Appointment> findAllByClient_UserId(UUID userId);

    default Appointment findAppointment(UUID appointmentId) {
        return findById(appointmentId).orElseThrow(
                () -> new NoSuchElementException("Appointment not found."));
    }
}
