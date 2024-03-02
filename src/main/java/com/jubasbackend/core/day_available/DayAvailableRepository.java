package com.jubasbackend.core.day_available;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DayAvailableRepository extends JpaRepository<DayAvailableEntity, Short> {

    public default int getQuantity() {
        var dayOfAttendance = findAll().stream().findFirst();
        if (dayOfAttendance.isPresent())
            return dayOfAttendance.get().getQuantity();

        return 0;
    }
}
