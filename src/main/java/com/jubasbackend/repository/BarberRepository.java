package com.jubasbackend.repository;

import com.jubasbackend.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BarberRepository extends JpaRepository<Barber, UUID> {
}
