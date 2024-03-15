package com.jubasbackend.core.non_service_day;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NonServiceDayRepository extends JpaRepository<NonServiceDayEntity, LocalDate> {
    default List<LocalDate> findDates() {
        return findAll().stream().map(NonServiceDayEntity::getDate).toList();
    }

    default List<LocalDate> findDateBetween(LocalDate from, LocalDate to) {
        return findAllByDateBetween(from, to).stream().map(NonServiceDayEntity::getDate).toList();
    }

    List<NonServiceDayEntity> findAllByDateBetween(LocalDate from, LocalDate to);
}
