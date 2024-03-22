package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.DayAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayAvailabilityRepository extends JpaRepository<DayAvailability, Long> {
    /**
     * @return Retorna a quantidade de dias em que a agenda ficará disponível.
     */
    default int findQuantity() {
        return findSingleEntity().getQuantity();
    }

    default DayAvailability findSingleEntity() {
        var entity = findById(1L);
        return entity.orElseGet(DayAvailability::new);
    }
}
