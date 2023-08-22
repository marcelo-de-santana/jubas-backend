package com.jubasbackend.service;

import com.jubasbackend.entity.Barber;
import com.jubasbackend.repository.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BarberService {
    @Autowired
    private BarberRepository barberRepository;

    public List<Barber> findALl() {
        return barberRepository.findAll();
    }

    public Barber findById(UUID id) {
        return barberRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Barber Not Found"));
    }

    public Barber save(Barber barber) {
        return barberRepository.save(barber);
    }
}
