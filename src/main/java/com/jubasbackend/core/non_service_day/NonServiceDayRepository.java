package com.jubasbackend.core.non_service_day;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NonServiceDayRepository extends JpaRepository<NonServiceDayEntity, LocalDate> {
    public default List<LocalDate> getDates() {
        return findAll().stream().map(NonServiceDayEntity::getDate).toList();
    }
}
