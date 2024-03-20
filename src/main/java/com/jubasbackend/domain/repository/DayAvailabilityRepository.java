package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.DayAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayAvailabilityRepository extends JpaRepository<DayAvailabilityEntity, Long> {
    /**
     * @return Retorna a quantidade de dias em que a agenda ficará disponível.
     */
    default int findQuantity() {
        return findSingleEntity().getQuantity();
    }

    default DayAvailabilityEntity findSingleEntity() {
        var entity = findById(1L);
        return entity.orElseGet(DayAvailabilityEntity::new);
    }
}
