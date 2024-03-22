package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.NonServiceDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NonServiceDayRepository extends JpaRepository<NonServiceDay, LocalDate> {
    default List<LocalDate> findDates() {
        return findAll().stream().map(NonServiceDay::getDate).toList();
    }

    default List<LocalDate> findDateBetween(LocalDate from, LocalDate to) {
        return findAllByDateBetween(from, to).stream().map(NonServiceDay::getDate).toList();
    }

    List<NonServiceDay> findAllByDateBetween(LocalDate from, LocalDate to);
}
